package com.example.aleksandr_rozkov_6020_pz2.controller;

import com.example.aleksandr_rozkov_6020_pz2.config.SecurityConfig;
import com.example.aleksandr_rozkov_6020_pz2.entity.Manul;
import com.example.aleksandr_rozkov_6020_pz2.security.CustomUserDetailsService;
import com.example.aleksandr_rozkov_6020_pz2.security.JwtAuthenticationFilter;
import com.example.aleksandr_rozkov_6020_pz2.security.JwtService;
import com.example.aleksandr_rozkov_6020_pz2.service.ManulService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ManulController.class)
@Import(SecurityConfig.class)
class ManulControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ManulService manulService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    @WithMockUser
    void getAllManulsShouldReturnList() throws Exception {
        Manul manul = new Manul();
        manul.setId(1L);
        manul.setName("Batu");

        when(manulService.getAllManuls(anyString(), anyString(), isNull(), isNull()))
                .thenReturn(List.of(manul));

        mockMvc.perform(get("/api/manuls"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllManulsWithAdminUserShouldWork() throws Exception {
        when(manulService.getAllManuls(anyString(), anyString(), isNull(), isNull()))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/manuls"))
                .andExpect(status().isOk());
    }
}