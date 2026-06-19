package com.example.aleksandr_rozkov_6020_pz2.service;

import com.example.aleksandr_rozkov_6020_pz2.dto.ManulRequest;
import com.example.aleksandr_rozkov_6020_pz2.entity.Manul;
import com.example.aleksandr_rozkov_6020_pz2.entity.User;
import com.example.aleksandr_rozkov_6020_pz2.repository.CommentRepository;
import com.example.aleksandr_rozkov_6020_pz2.repository.LikeRepository;
import com.example.aleksandr_rozkov_6020_pz2.repository.ManulRepository;
import com.example.aleksandr_rozkov_6020_pz2.repository.SuggestionRepository;
import com.example.aleksandr_rozkov_6020_pz2.repository.UserRepository;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
public class ManulService {
    private final ManulRepository manulRepository;
    private final SuggestionRepository suggestionRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;

    public ManulService(ManulRepository manulRepository, SuggestionRepository suggestionRepository, UserRepository userRepository,
                        LikeRepository likeRepository, CommentRepository commentRepository) {
        this.manulRepository = manulRepository;
        this.suggestionRepository = suggestionRepository;
        this.userRepository = userRepository;
        this.likeRepository = likeRepository;
        this.commentRepository = commentRepository;
    }

    public Object getAllManuls(String sortBy, String order, Integer page, Integer limit) {
        Sort sort = Sort.by("desc".equalsIgnoreCase(order) ? Sort.Direction.DESC : Sort.Direction.ASC, safeSort(sortBy));
        if (page == null || limit == null) {
            return manulRepository.findAll(sort);
        }
        int pageIndex = Math.max(page, 1) - 1;
        Page<Manul> result = manulRepository.findAll(PageRequest.of(pageIndex, limit, sort));
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("items", result.getContent());
        response.put("total", result.getTotalElements());
        response.put("pages", result.getTotalPages());
        response.put("page", page);
        return response;
    }

    public Map<String, Object> getManulById(Long id, Authentication authentication) {
        Manul manul = manulRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Manul not found"));
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("id", manul.getId());
        response.put("name", manul.getName());
        response.put("photoUrl", manul.getPhotoUrl());
        response.put("shortDescription", manul.getShortDescription());
        response.put("longStory", manul.getLongStory());
        response.put("locationType", manul.getLocationType());
        response.put("zooId", manul.getZooId());
        response.put("region", manul.getRegion());
        response.put("likesCount", (int) likeRepository.countByManulId(manul.getId()));
        response.put("favoritesCount", value(manul.getFavoritesCount()));
        response.put("createdAt", manul.getCreatedAt());
        response.put("likedByCurrentUser", isLikedByCurrentUser(manul.getId(), authentication));
        return response;
    }

    @Transactional
    public Manul createManul(ManulRequest request) {
        Manul manul = new Manul();
        apply(manul, request, true);
        return manulRepository.save(manul);
    }

    @Transactional
    public Manul updateManul(Long id, ManulRequest request) {
        Manul manul = manulRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Manul not found"));
        apply(manul, request, false);
        return manulRepository.save(manul);
    }

    @Transactional
    public void deleteManul(Long id) {
        likeRepository.deleteByManulId(id);
        commentRepository.deleteByManulId(id);
        manulRepository.deleteById(id);
    }

    @Transactional
    public void incrementLikes(Long id) {
        Manul manul = manulRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Manul not found"));
        manul.setLikesCount(value(manul.getLikesCount()) + 1);
        manulRepository.save(manul);
    }

    private void apply(Manul manul, ManulRequest request, boolean creating) {
        if (request.getName() != null) manul.setName(request.getName());
        if (request.getPhotoUrl() != null) manul.setPhotoUrl(request.getPhotoUrl());
        if (request.getShortDescription() != null) manul.setShortDescription(request.getShortDescription());
        if (request.getLongStory() != null) manul.setLongStory(request.getLongStory());
        if (request.getLocationType() != null) manul.setLocationType(request.getLocationType());
        if (request.getZooId() != null) manul.setZooId(request.getZooId());
        if (request.getRegion() != null) manul.setRegion(request.getRegion());
        if (request.getLikesCount() != null) manul.setLikesCount(request.getLikesCount());
        if (request.getFavoritesCount() != null) manul.setFavoritesCount(request.getFavoritesCount());
        if (request.getCreatedAt() != null) manul.setCreatedAt(request.getCreatedAt());
        if (creating) {
            if (manul.getLikesCount() == null) manul.setLikesCount(0);
            if (manul.getFavoritesCount() == null) manul.setFavoritesCount(0);
            if (manul.getCreatedAt() == null) manul.setCreatedAt(LocalDate.now().toString());
        }
    }

    private boolean isLikedByCurrentUser(Long manulId, Authentication authentication) {
        if (authentication == null || authentication.getName() == null) return false;
        Optional<User> user = userRepository.findByEmail(authentication.getName());
        return user.filter(value -> likeRepository.existsByUserIdAndManulId(value.getId(), manulId)).isPresent();
    }

    private String safeSort(String sortBy) {
        if (List.of("name", "likesCount", "createdAt").contains(sortBy)) return sortBy;
        return "createdAt";
    }

    private int value(Integer number) {
        return number == null ? 0 : number;
    }
}
