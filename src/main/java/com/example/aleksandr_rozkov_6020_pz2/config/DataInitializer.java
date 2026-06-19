package com.example.aleksandr_rozkov_6020_pz2.config;

import com.example.aleksandr_rozkov_6020_pz2.entity.Like;
import com.example.aleksandr_rozkov_6020_pz2.entity.Manul;
import com.example.aleksandr_rozkov_6020_pz2.entity.Suggestion;
import com.example.aleksandr_rozkov_6020_pz2.entity.User;
import com.example.aleksandr_rozkov_6020_pz2.repository.LikeRepository;
import com.example.aleksandr_rozkov_6020_pz2.repository.ManulRepository;
import com.example.aleksandr_rozkov_6020_pz2.repository.SuggestionRepository;
import com.example.aleksandr_rozkov_6020_pz2.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;

@Component
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final ManulRepository manulRepository;
    private final SuggestionRepository suggestionRepository;
    private final LikeRepository likeRepository;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper mapper;

    public DataInitializer(UserRepository userRepository, ManulRepository manulRepository, SuggestionRepository suggestionRepository,
                           LikeRepository likeRepository, PasswordEncoder passwordEncoder, ObjectMapper mapper) {
        this.userRepository = userRepository;
        this.manulRepository = manulRepository;
        this.suggestionRepository = suggestionRepository;
        this.likeRepository = likeRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        createUser("admin@manuls.com", "admin123", "admin");
        createUser("user@manuls.com", "user123", "user");

        if (manulRepository.count() > 0) {
            migrateLikesFromSuggestions();
            updateLikesCount();
            return;
        }

        try (InputStream inputStream = new ClassPathResource("db.json").getInputStream()) {
            JsonNode root = mapper.readTree(inputStream);
            for (JsonNode node : root.get("manuls")) {
                Manul manul = new Manul();
                manul.setName(text(node, "name"));
                manul.setPhotoUrl(text(node, "photoUrl"));
                manul.setShortDescription(text(node, "shortDescription"));
                manul.setLongStory(text(node, "longStory"));
                manul.setLocationType(text(node, "locationType"));
                manul.setZooId(node.hasNonNull("zooId") ? node.get("zooId").asLong() : null);
                manul.setRegion(text(node, "region"));
                manul.setLikesCount(node.has("likesCount") ? node.get("likesCount").asInt() : 0);
                manul.setFavoritesCount(node.has("favoritesCount") ? node.get("favoritesCount").asInt() : 0);
                manul.setCreatedAt(text(node, "createdAt"));
                manulRepository.save(manul);
            }

            if (root.has("suggestions")) {
                for (JsonNode node : root.get("suggestions")) {
                    if (!node.hasNonNull("manulId")) continue;
                    Long userId = node.hasNonNull("userId") ? node.get("userId").asLong() : 2L;
                    Long manulId = node.get("manulId").asLong();
                    String type = text(node, "type");
                    if ("LIKE".equalsIgnoreCase(type)) {
                        if (!likeRepository.existsByUserIdAndManulId(userId, manulId)) {
                            Like like = new Like();
                            like.setUserId(userId);
                            like.setManulId(manulId);
                            like.setCreatedAt(text(node, "createdAt"));
                            likeRepository.save(like);
                        }
                    } else {
                        Suggestion suggestion = new Suggestion();
                        suggestion.setUserId(userId);
                        suggestion.setManulId(manulId);
                        suggestion.setType(type);
                        suggestion.setContent(text(node, "content"));
                        suggestion.setStatus(text(node, "status") == null ? "APPROVED" : text(node, "status"));
                        suggestion.setCreatedAt(text(node, "createdAt"));
                        suggestionRepository.save(suggestion);
                    }
                }
            }

            updateLikesCount();
        }
    }

    private void migrateLikesFromSuggestions() {
        suggestionRepository.findByTypeOrderByIdDesc("LIKE").forEach(suggestion -> {
            if (!likeRepository.existsByUserIdAndManulId(suggestion.getUserId(), suggestion.getManulId())) {
                Like like = new Like();
                like.setUserId(suggestion.getUserId());
                like.setManulId(suggestion.getManulId());
                like.setCreatedAt(suggestion.getCreatedAt());
                likeRepository.save(like);
            }
        });
    }

    private void updateLikesCount() {
        manulRepository.findAll().forEach(manul -> {
            manul.setLikesCount((int) likeRepository.countByManulId(manul.getId()));
            manulRepository.save(manul);
        });
    }

    private void createUser(String email, String password, String role) {
        if (userRepository.existsByEmail(email)) return;
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        userRepository.save(user);
    }

    private String text(JsonNode node, String field) {
        return node.hasNonNull(field) ? node.get(field).asText() : null;
    }
}
