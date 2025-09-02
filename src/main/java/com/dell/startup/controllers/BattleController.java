package com.dell.startup.controllers;

import com.dell.startup.models.Battle;
import com.dell.startup.services.BattleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/battles")
public class BattleController {

    @Autowired
    private BattleService battleService;

    @PostMapping("/generate")
    public List<Battle> generateBattles() {
        return battleService.generateBattles(1);
    }

    @PutMapping("/{battleId}/winner")
    public Battle declareWinner(@PathVariable Long battleId) {
        return battleService.declareWinner(battleId);
    }

    @GetMapping
    public List<Battle> getAllBattles() {
        return battleService.getAllBattles();
    }

    @GetMapping("/round/{round}")
    public List<Battle> getBattlesByRound(@PathVariable int round) {
        return battleService.getBattlesByRound(round);
    }

    @PutMapping("/{battleId}/finalize")
    public Battle finalizeBattle(@PathVariable Long battleId) {
        return battleService.finalizeBattleIfReady(battleId);
    }

    @PostMapping("/advance")
    public ResponseEntity<String> advanceToNextRound() {
        battleService.advanceToNextRoundIfNeeded();
        return ResponseEntity.ok("Round verified.");
    }

    @PutMapping("/finalize-ready")
    public List<Battle> finalizeReadyBattles() {
        return battleService.finalizeAllReadyBattles();
    }

    // @PostMapping("/reset")
    // public ResponseEntity<String> resetBattles() {
    //     battleService.resetBattles();
    //     return ResponseEntity.ok("reseted battles");
    // }

}
