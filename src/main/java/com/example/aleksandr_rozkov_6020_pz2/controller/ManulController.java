package com.example.aleksandr_rozkov_6020_pz2.controller;

import com.example.aleksandr_rozkov_6020_pz2.entity.Manul;
import com.example.aleksandr_rozkov_6020_pz2.service.ManulService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/manuls")
public class ManulController {

    private final ManulService manulService;

    public ManulController(ManulService manulService) {
        this.manulService = manulService;
    }

    @GetMapping
    public List<Manul> getAllManuls() {
        return manulService.getAllManuls();
    }

    @GetMapping("/{id}")
    public Manul getManulById(@PathVariable Long id) {
        return manulService.getManulById(id);
    }

    @PostMapping
    public Manul createManul(@RequestBody Manul manul) {
        return manulService.createManul(manul);
    }

    @PutMapping("/{id}")
    public Manul updateManul(@PathVariable Long id, @RequestBody Manul manul) {
        return manulService.updateManul(id, manul);
    }

    @DeleteMapping("/{id}")
    public void deleteManul(@PathVariable Long id) {
        manulService.deleteManul(id);
    }
}