package com.example.aleksandr_rozkov_6020_pz2.service;

import com.example.aleksandr_rozkov_6020_pz2.entity.Manul;
import com.example.aleksandr_rozkov_6020_pz2.repository.ManulRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManulService {

    private final ManulRepository manulRepository;

    public ManulService(ManulRepository manulRepository) {
        this.manulRepository = manulRepository;
    }

    public List<Manul> getAllManuls() {
        return manulRepository.findAll();
    }

    public Manul getManulById(Long id) {
        return manulRepository.findById(id).orElse(null);
    }

    public Manul createManul(Manul manul) {
        return manulRepository.save(manul);
    }

    public Manul updateManul(Long id, Manul updatedManul) {
        Manul manul = manulRepository.findById(id).orElse(null);

        if (manul == null) {
            return null;
        }

        manul.setName(updatedManul.getName());
        manul.setPhotoUrl(updatedManul.getPhotoUrl());
        manul.setShortDescription(updatedManul.getShortDescription());
        manul.setLongStory(updatedManul.getLongStory());
        manul.setLocationType(updatedManul.getLocationType());
        manul.setZooId(updatedManul.getZooId());
        manul.setRegion(updatedManul.getRegion());
        manul.setLikesCount(updatedManul.getLikesCount());
        manul.setFavoritesCount(updatedManul.getFavoritesCount());
        manul.setCreatedAt(updatedManul.getCreatedAt());

        return manulRepository.save(manul);
    }

    public void deleteManul(Long id) {
        manulRepository.deleteById(id);
    }
}