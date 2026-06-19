package com.example.aleksandr_rozkov_6020_pz2.controller;

import com.example.aleksandr_rozkov_6020_pz2.service.LikeService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/manuls/{manulId}/like")
public class LikeController {
    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping
    public Map<String, Object> likeManul(@PathVariable Long manulId, Authentication authentication) {
        return likeService.likeManul(manulId, authentication);
    }

    @DeleteMapping
    public Map<String, Object> unlikeManul(@PathVariable Long manulId, Authentication authentication) {
        return likeService.unlikeManul(manulId, authentication);
    }
}
