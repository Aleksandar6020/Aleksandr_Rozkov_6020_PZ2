package com.example.aleksandr_rozkov_6020_pz2.service;

import com.example.aleksandr_rozkov_6020_pz2.dto.ManulRequest;
import com.example.aleksandr_rozkov_6020_pz2.entity.Manul;
import com.example.aleksandr_rozkov_6020_pz2.repository.CommentRepository;
import com.example.aleksandr_rozkov_6020_pz2.repository.LikeRepository;
import com.example.aleksandr_rozkov_6020_pz2.repository.ManulRepository;
import com.example.aleksandr_rozkov_6020_pz2.repository.SuggestionRepository;
import com.example.aleksandr_rozkov_6020_pz2.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ManulServiceTest {
    @Mock private ManulRepository manulRepository;
    @Mock private SuggestionRepository suggestionRepository;
    @Mock private UserRepository userRepository;
    @Mock private LikeRepository likeRepository;
    @Mock private CommentRepository commentRepository;

    @InjectMocks
    private ManulService manulService;

    @Test
    void createManulShouldSaveNewManul() {
        ManulRequest request = new ManulRequest();
        request.setName("Batu");
        request.setPhotoUrl("photo.jpg");
        request.setShortDescription("Short");
        request.setLongStory("Long story");
        request.setLocationType("ZOO");
        request.setRegion("Belgrade");

        when(manulRepository.save(any(Manul.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Manul result = manulService.createManul(request);

        assertEquals("Batu", result.getName());
        assertEquals("ZOO", result.getLocationType());
        assertEquals(0, result.getLikesCount());
        assertEquals(0, result.getFavoritesCount());
        assertNotNull(result.getCreatedAt());
        verify(manulRepository).save(any(Manul.class));
    }

    @Test
    void updateManulShouldChangeExistingManul() {
        Manul existing = new Manul();
        existing.setId(1L);
        existing.setName("Old name");

        ManulRequest request = new ManulRequest();
        request.setName("New name");

        when(manulRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(manulRepository.save(any(Manul.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Manul result = manulService.updateManul(1L, request);

        assertEquals("New name", result.getName());
        verify(manulRepository).findById(1L);
        verify(manulRepository).save(existing);
    }

    @Test
    void deleteManulShouldDeleteRelatedData() {
        manulService.deleteManul(1L);

        verify(likeRepository).deleteByManulId(1L);
        verify(commentRepository).deleteByManulId(1L);
        verify(manulRepository).deleteById(1L);
    }
}