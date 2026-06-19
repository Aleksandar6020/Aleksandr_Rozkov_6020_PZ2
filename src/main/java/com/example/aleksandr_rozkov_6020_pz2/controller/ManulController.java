package com.example.aleksandr_rozkov_6020_pz2.controller;

import com.example.aleksandr_rozkov_6020_pz2.dto.ManulRequest;
import com.example.aleksandr_rozkov_6020_pz2.entity.Manul;
import com.example.aleksandr_rozkov_6020_pz2.service.ManulService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/manuls")
public class ManulController {
    private final ManulService manulService;

    public ManulController(ManulService manulService) {
        this.manulService = manulService;
    }

    @GetMapping
    public Object getAllManuls(@RequestParam(required = false, defaultValue = "createdAt") String sortBy,
                               @RequestParam(required = false, defaultValue = "desc") String order,
                               @RequestParam(required = false) Integer page,
                               @RequestParam(required = false) Integer limit) {
        return manulService.getAllManuls(sortBy, order, page, limit);
    }

    @GetMapping("/{id}")
    public Map<String, Object> getManulById(@PathVariable Long id, Authentication authentication) {
        return manulService.getManulById(id, authentication);
    }

    @PostMapping
    public Manul createManul(@RequestBody ManulRequest request) {
        return manulService.createManul(request);
    }

    @PatchMapping("/{id}")
    public Manul updateManul(@PathVariable Long id, @RequestBody ManulRequest request) {
        return manulService.updateManul(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteManul(@PathVariable Long id) {
        manulService.deleteManul(id);
    }
}
