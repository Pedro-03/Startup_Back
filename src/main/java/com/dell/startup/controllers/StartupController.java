package com.dell.startup.controllers;

import com.dell.startup.services.StartupService;
import com.dell.startup.dto.StartupStatsDTO;
import com.dell.startup.dto.WinnerDTO;
import com.dell.startup.models.Startup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.dell.startup.repositories.StartupRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/startups")
public class StartupController {

    @Autowired
    private StartupRepository startupRepository;

    @Autowired
    private StartupService startupService;

    @PostMapping
    public Startup createStartup(@RequestBody Startup startup) {
        return startupService.createStartup(startup);
    }

    @PostMapping("/batch")
    public List<Startup> createMultiple(@RequestBody List<Startup> startups) {
        return startupService.createMultipleStartups(startups);
    }

    @GetMapping
    public List<Startup> getAllStartups() {
        return startupService.getAllStartups();
    }

    @GetMapping("/{id}")
    public Optional<Startup> getStartupById(@PathVariable Long id) {
        return startupService.getStartupById(id);
    }

    @PutMapping("/{id}")
    public Startup updateStartup(@PathVariable Long id, @RequestBody Startup startup) {
        return startupService.updateStartup(id, startup);
    }

    @DeleteMapping("/{id}")
    public void deleteStartup(@PathVariable Long id) {
        startupService.deleteStartup(id);
    }

    @DeleteMapping("/reset")
    public void deleteAllStartups() {
        startupService.resetEverything();
    }

    @GetMapping("/winner")
    public ResponseEntity<?> getWinner() {
        Optional<WinnerDTO> winner = startupService.getWinner();

        return winner.<ResponseEntity<?>>map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Winner not found"));
    }

    @GetMapping("/stats")
    public List<StartupStatsDTO> getStartupStats() {
        return startupRepository.fetchStartupStats();
    }

}
