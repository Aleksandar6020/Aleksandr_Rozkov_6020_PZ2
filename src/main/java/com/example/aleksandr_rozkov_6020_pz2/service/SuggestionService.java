package com.example.aleksandr_rozkov_6020_pz2.service;

import com.example.aleksandr_rozkov_6020_pz2.dto.SuggestionRequest;
import com.example.aleksandr_rozkov_6020_pz2.entity.Suggestion;
import com.example.aleksandr_rozkov_6020_pz2.entity.User;
import com.example.aleksandr_rozkov_6020_pz2.repository.ManulRepository;
import com.example.aleksandr_rozkov_6020_pz2.repository.SuggestionRepository;
import com.example.aleksandr_rozkov_6020_pz2.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class SuggestionService {
    private final SuggestionRepository suggestionRepository;
    private final UserRepository userRepository;
    private final ManulRepository manulRepository;
    private final LikeService likeService;

    public SuggestionService(SuggestionRepository suggestionRepository, UserRepository userRepository, ManulRepository manulRepository, LikeService likeService) {
        this.suggestionRepository = suggestionRepository;
        this.userRepository = userRepository;
        this.manulRepository = manulRepository;
        this.likeService = likeService;
    }

    public List<Suggestion> getSuggestions(String type) {
        if (type == null || type.isBlank()) return suggestionRepository.findAll();
        return suggestionRepository.findByTypeOrderByIdDesc(type);
    }

    @Transactional
    public Suggestion createSuggestion(SuggestionRequest request, Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        if (!manulRepository.existsById(request.getManulId())) {
            throw new NoSuchElementException("Manul not found");
        }

        String type = request.getType() == null ? "STORY" : request.getType().toUpperCase();
        if ("LIKE".equals(type)) {
            likeService.likeManul(request.getManulId(), authentication);
        }

        Suggestion suggestion = new Suggestion();
        suggestion.setUserId(user.getId());
        suggestion.setManulId(request.getManulId());
        suggestion.setType(type);
        suggestion.setContent(request.getContent());
        suggestion.setStatus(request.getStatus() == null ? ("LIKE".equals(type) ? "APPROVED" : "PENDING") : request.getStatus());
        suggestion.setCreatedAt(Instant.now().toString());
        Suggestion saved = suggestionRepository.save(suggestion);

        return saved;
    }

    @Transactional
    public Suggestion updateSuggestion(Long id, SuggestionRequest request) {
        Suggestion suggestion = suggestionRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Suggestion not found"));
        if (request.getStatus() != null) suggestion.setStatus(request.getStatus());
        if (request.getContent() != null) suggestion.setContent(request.getContent());
        return suggestionRepository.save(suggestion);
    }
}
