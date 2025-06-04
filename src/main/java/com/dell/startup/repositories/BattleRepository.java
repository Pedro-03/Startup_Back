package com.dell.startup.repositories;

import com.dell.startup.models.Battle;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BattleRepository extends JpaRepository<Battle, Long> {
    List<Battle> findByRound(int round);
    List<Battle> findByFinishedFalse();

}
