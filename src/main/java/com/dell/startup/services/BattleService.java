package com.dell.startup.services;

import com.dell.startup.helpers.BattleHelper;
import com.dell.startup.models.Battle;
import com.dell.startup.models.Startup;
import com.dell.startup.repositories.BattleRepository;
import com.dell.startup.repositories.StartupRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.*;

@Service
public class BattleService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private BattleRepository battleRepository;

    @Autowired
    private StartupRepository startupRepository;

    @Autowired
    private EventService eventService;

    public List<Battle> generateBattles(int round) {
        List<Startup> startups = startupRepository.findAll();

        if (startups.size() < 4 || startups.size() > 8 || startups.size() % 2 != 0) {
            throw new IllegalArgumentException("Number of startups must be even and between 4 and 8.");
        }

        Collections.shuffle(startups);
        List<Battle> battles = new ArrayList<>();

        for (int i = 0; i < startups.size(); i += 2) {
            Battle battle = new Battle(startups.get(i), startups.get(i + 1), round);
            battles.add(battle);
        }

        return battleRepository.saveAll(battles);
    }

    public Battle declareWinner(Long battleId) {
        Optional<Battle> optBattle = battleRepository.findById(battleId);
        if (!optBattle.isPresent()) {
            throw new RuntimeException("Battle not found");
        }

        Battle battle = optBattle.get();
        Startup winner = BattleHelper.decideWinner(battle);

        battle.setWinner(winner);
        battle.setFinished(true);

        winner.setPoints(winner.getPoints() + 30);
        startupRepository.save(winner);

        return battleRepository.save(battle);
    }

    public Battle finalizeBattleIfReady(Long battleId) {
        Optional<Battle> optBattle = battleRepository.findById(battleId);
        if (!optBattle.isPresent()) {
            throw new RuntimeException("Battle not found");
        }

        Battle battle = optBattle.get();

        if (!eventService.isBattleReadyForFinalization(battle)) {
            throw new RuntimeException("Battle not ready for finalization. Missing events.");
        }

        return declareWinner(battleId);
    }

    public void advanceToNextRoundIfNeeded() {
        List<Battle> allBattles = battleRepository.findAll();

        int currentRound = 1;
        for (Battle b : allBattles) {
            if (b.getRound() > currentRound) {
                currentRound = b.getRound();
            }
        }

        List<Battle> currentRoundBattles = new ArrayList<>();
        for (Battle b : allBattles) {
            if (b.getRound() == currentRound) {
                currentRoundBattles.add(b);
            }
        }

        boolean allFinished = true;
        for (Battle b : currentRoundBattles) {
            if (!b.isFinished()) {
                allFinished = false;
                break;
            }
        }

        if (!allFinished) return;

        List<Startup> winners = new ArrayList<>();
        for (Battle b : currentRoundBattles) {
            Startup winner = b.getWinner();
            if (winner != null) {
                winners.add(winner);
            }
        }

        if (winners.size() == 1) {
            System.out.println("Champion found: " + winners.get(0).getName());
            return;
        }

        Collections.shuffle(winners);
        List<Battle> newBattles = new ArrayList<>();

        for (int i = 0; i < winners.size(); i += 2) {
            Startup a = winners.get(i);
            Startup b = winners.get(i + 1);
            newBattles.add(new Battle(a, b, currentRound + 1));
        }

        battleRepository.saveAll(newBattles);
    }

    public List<Battle> finalizeAllReadyBattles() {
        List<Battle> unfinishedBattles = battleRepository.findByFinishedFalse();
        List<Battle> finalized = new ArrayList<>();

        for (int i = 0; i < unfinishedBattles.size(); i++) {
            Battle battle = unfinishedBattles.get(i);
            if (eventService.isBattleReadyForFinalization(battle)) {
                Battle finalizedBattle = declareWinner(battle.getId());
                finalized.add(finalizedBattle);
            }
        }

        return finalized;
    }

    @Transactional
    public void resetBattles() {
        battleRepository.deleteAll();
        entityManager.createNativeQuery("ALTER SEQUENCE battle_id_seq RESTART WITH 1").executeUpdate();
    }

    public List<Battle> getAllBattles() {
        return battleRepository.findAll();
    }

    public List<Battle> getBattlesByRound(int round) {
        return battleRepository.findByRound(round);
    }
}
