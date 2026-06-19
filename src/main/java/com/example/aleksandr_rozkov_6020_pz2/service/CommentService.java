package com.example.aleksandr_rozkov_6020_pz2.service;

import com.example.aleksandr_rozkov_6020_pz2.dto.CommentRequest;
import com.example.aleksandr_rozkov_6020_pz2.entity.Comment;
import com.example.aleksandr_rozkov_6020_pz2.entity.User;
import com.example.aleksandr_rozkov_6020_pz2.repository.CommentRepository;
import com.example.aleksandr_rozkov_6020_pz2.repository.ManulRepository;
import com.example.aleksandr_rozkov_6020_pz2.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final ManulRepository manulRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository, ManulRepository manulRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.manulRepository = manulRepository;
        this.userRepository = userRepository;
    }

    public List<Map<String, Object>> getComments(Long manulId) {
        if (!manulRepository.existsById(manulId)) {
            throw new NoSuchElementException("Manul not found");
        }
        return commentRepository.findByManulIdOrderByIdDesc(manulId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public Map<String, Object> createComment(Long manulId, CommentRequest request, Authentication authentication) {
        if (request.getContent() == null || request.getContent().isBlank()) {
            throw new IllegalArgumentException("Comment cannot be empty");
        }
        if (!manulRepository.existsById(manulId)) {
            throw new NoSuchElementException("Manul not found");
        }
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        Comment comment = new Comment();
        comment.setUserId(user.getId());
        comment.setManulId(manulId);
        comment.setContent(request.getContent().trim());
        comment.setCreatedAt(Instant.now().toString());
        return toResponse(commentRepository.save(comment));
    }

    @Transactional
    public void deleteComment(Long id, Authentication authentication) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Comment not found"));
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        boolean owner = Objects.equals(comment.getUserId(), user.getId());
        boolean admin = "admin".equalsIgnoreCase(user.getRole());
        if (!owner && !admin) {
            throw new IllegalArgumentException("You cannot delete this comment");
        }
        commentRepository.delete(comment);
    }

    private Map<String, Object> toResponse(Comment comment) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("id", comment.getId());
        response.put("userId", comment.getUserId());
        response.put("manulId", comment.getManulId());
        response.put("content", comment.getContent());
        response.put("createdAt", comment.getCreatedAt());
        response.put("authorEmail", userRepository.findById(comment.getUserId()).map(User::getEmail).orElse("unknown"));
        return response;
    }
}
