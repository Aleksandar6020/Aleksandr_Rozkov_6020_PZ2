package com.example.aleksandr_rozkov_6020_pz2.controller;

import com.example.aleksandr_rozkov_6020_pz2.dto.CommentRequest;
import com.example.aleksandr_rozkov_6020_pz2.service.CommentService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/api/manuls/{manulId}/comments")
    public List<Map<String, Object>> getComments(@PathVariable Long manulId) {
        return commentService.getComments(manulId);
    }

    @PostMapping("/api/manuls/{manulId}/comments")
    public Map<String, Object> createComment(@PathVariable Long manulId, @RequestBody CommentRequest request, Authentication authentication) {
        return commentService.createComment(manulId, request, authentication);
    }

    @DeleteMapping("/api/comments/{id}")
    public void deleteComment(@PathVariable Long id, Authentication authentication) {
        commentService.deleteComment(id, authentication);
    }
}
