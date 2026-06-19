package com.example.aleksandr_rozkov_6020_pz2.service;

import com.example.aleksandr_rozkov_6020_pz2.entity.Like;
import com.example.aleksandr_rozkov_6020_pz2.entity.Manul;
import com.example.aleksandr_rozkov_6020_pz2.entity.User;
import com.example.aleksandr_rozkov_6020_pz2.repository.LikeRepository;
import com.example.aleksandr_rozkov_6020_pz2.repository.ManulRepository;
import com.example.aleksandr_rozkov_6020_pz2.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class LikeService {
    private final LikeRepository likeRepository;
    private final ManulRepository manulRepository;
    private final UserRepository userRepository;

    public LikeService(LikeRepository likeRepository, ManulRepository manulRepository, UserRepository userRepository) {
        this.likeRepository = likeRepository;
        this.manulRepository = manulRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Map<String, Object> likeManul(Long manulId, Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        Manul manul = manulRepository.findById(manulId)
                .orElseThrow(() -> new NoSuchElementException("Manul not found"));

        if (likeRepository.existsByUserIdAndManulId(user.getId(), manulId)) {
            return response(manulId, true);
        }

        Like like = new Like();
        like.setUserId(user.getId());
        like.setManulId(manulId);
        like.setCreatedAt(Instant.now().toString());
        likeRepository.save(like);

        manul.setLikesCount((int) likeRepository.countByManulId(manulId));
        manulRepository.save(manul);

        return response(manulId, true);
    }

    @Transactional
    public Map<String, Object> unlikeManul(Long manulId, Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        likeRepository.findByUserIdAndManulId(user.getId(), manulId).ifPresent(likeRepository::delete);

        Manul manul = manulRepository.findById(manulId)
                .orElseThrow(() -> new NoSuchElementException("Manul not found"));
        manul.setLikesCount((int) likeRepository.countByManulId(manulId));
        manulRepository.save(manul);

        return response(manulId, false);
    }

    public boolean isLikedByCurrentUser(Long manulId, Authentication authentication) {
        if (authentication == null || authentication.getName() == null) return false;
        return userRepository.findByEmail(authentication.getName())
                .map(user -> likeRepository.existsByUserIdAndManulId(user.getId(), manulId))
                .orElse(false);
    }

    public int countLikes(Long manulId) {
        return (int) likeRepository.countByManulId(manulId);
    }

    private Map<String, Object> response(Long manulId, boolean liked) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("manulId", manulId);
        response.put("liked", liked);
        response.put("likesCount", countLikes(manulId));
        return response;
    }
}
