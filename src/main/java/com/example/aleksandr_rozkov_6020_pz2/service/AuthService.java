package com.example.aleksandr_rozkov_6020_pz2.service;

import com.example.aleksandr_rozkov_6020_pz2.dto.AuthResponse;
import com.example.aleksandr_rozkov_6020_pz2.dto.LoginRequest;
import com.example.aleksandr_rozkov_6020_pz2.dto.RegisterRequest;
import com.example.aleksandr_rozkov_6020_pz2.entity.User;
import com.example.aleksandr_rozkov_6020_pz2.repository.UserRepository;
import com.example.aleksandr_rozkov_6020_pz2.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("user");
        userRepository.save(user);
        return AuthResponse.from(user, jwtService.generateToken(user));
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }
        return AuthResponse.from(user, jwtService.generateToken(user));
    }
}
