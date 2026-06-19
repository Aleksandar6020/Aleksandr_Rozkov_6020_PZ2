package com.example.aleksandr_rozkov_6020_pz2.controller;

import com.example.aleksandr_rozkov_6020_pz2.dto.AuthResponse;
import com.example.aleksandr_rozkov_6020_pz2.dto.LoginRequest;
import com.example.aleksandr_rozkov_6020_pz2.dto.RegisterRequest;
import com.example.aleksandr_rozkov_6020_pz2.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }
}
