package com.example.aleksandr_rozkov_6020_pz2.controller;

import com.example.aleksandr_rozkov_6020_pz2.dto.SuggestionRequest;
import com.example.aleksandr_rozkov_6020_pz2.entity.Suggestion;
import com.example.aleksandr_rozkov_6020_pz2.service.SuggestionService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suggestions")
public class SuggestionController {
    private final SuggestionService suggestionService;

    public SuggestionController(SuggestionService suggestionService) {
        this.suggestionService = suggestionService;
    }

    @GetMapping
    public List<Suggestion> getSuggestions(@RequestParam(required = false) String type) {
        return suggestionService.getSuggestions(type);
    }

    @PostMapping
    public Suggestion createSuggestion(@RequestBody SuggestionRequest request, Authentication authentication) {
        return suggestionService.createSuggestion(request, authentication);
    }

    @PatchMapping("/{id}")
    public Suggestion updateSuggestion(@PathVariable Long id, @RequestBody SuggestionRequest request) {
        return suggestionService.updateSuggestion(id, request);
    }
}
