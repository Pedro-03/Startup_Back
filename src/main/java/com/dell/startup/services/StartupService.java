package com.dell.startup.services;

import com.dell.startup.dto.WinnerDTO;
import com.dell.startup.models.Battle;
import com.dell.startup.models.Startup;
import com.dell.startup.repositories.BattleRepository;
import com.dell.startup.repositories.EventRepository;
import com.dell.startup.repositories.StartupRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class StartupService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private StartupRepository startupRepository;

    @Autowired
    private BattleRepository battleRepository;

    @Autowired
    private EventRepository eventRepository;

    public Startup createStartup(Startup startup) {
        if (!startup.getName().matches("^[A-Za-z\\s]+$")) {
            throw new IllegalArgumentException("Startup name must not contain numbers or special characters.");
        }
        if (!startup.getSlogan().matches("^[A-Za-z\\s]+$")) {
            throw new IllegalArgumentException("Slogan must not contain numbers or special characters.");
        }
    
        return startupRepository.save(startup);
    }
    

    public List<Startup> createMultipleStartups(List<Startup> startups) {
        return startupRepository.saveAll(startups);
    }

    public List<Startup> getAllStartups() {
        return startupRepository.findAll();
    }

    public Optional<Startup> getStartupById(Long id) {
        return startupRepository.findById(id);
    }

    public void deleteStartup(Long id) {
        startupRepository.deleteById(id);
    }

    @Transactional
    public void resetEverything() {
        eventRepository.deleteAll();
        battleRepository.deleteAll();
        startupRepository.deleteAll();

        entityManager.createNativeQuery("ALTER SEQUENCE event_id_seq RESTART WITH 1").executeUpdate();
        entityManager.createNativeQuery("ALTER SEQUENCE battle_id_seq RESTART WITH 1").executeUpdate();
        entityManager.createNativeQuery("ALTER SEQUENCE startup_id_seq RESTART WITH 1").executeUpdate();
    }

    public Startup updateStartup(Long id, Startup updated) {
        return startupRepository.findById(id).map(startup -> {
            startup.setName(updated.getName());
            startup.setSlogan(updated.getSlogan());
            startup.setFoundationDate(updated.getFoundationDate());
            return startupRepository.save(startup);
        }).orElseThrow(() -> new RuntimeException("Startup not found"));
    }

    public Optional<WinnerDTO> getWinner() {
        List<Battle> battles = battleRepository.findAll();

        if (battles.isEmpty())
            return Optional.empty();

        Battle latest = battles.get(0);
        for (Battle b : battles) {
            if (b.getRound() > latest.getRound()) {
                latest = b;
            }
        }

        Startup winner = latest.getWinner();
        if (winner == null)
            return Optional.empty();

        return Optional.of(new WinnerDTO(winner.getId(), winner.getName(), winner.getSlogan()));
    }

}
