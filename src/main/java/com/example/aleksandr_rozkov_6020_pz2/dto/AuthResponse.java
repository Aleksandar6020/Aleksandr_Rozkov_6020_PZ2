package com.example.aleksandr_rozkov_6020_pz2.dto;

import com.example.aleksandr_rozkov_6020_pz2.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponse {
    private SafeUser user;
    private String token;

    public static AuthResponse from(User user, String token) {
        return new AuthResponse(new SafeUser(user.getId(), user.getEmail(), user.getRole()), token);
    }

    @Getter
    @AllArgsConstructor
    public static class SafeUser {
        private Long id;
        private String email;
        private String role;
    }
}
