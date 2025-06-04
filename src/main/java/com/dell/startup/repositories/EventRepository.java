package com.dell.startup.repositories;

import com.dell.startup.models.Event;
import com.dell.startup.models.EventType;
import com.dell.startup.models.Startup;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
    boolean existsByStartupAndTypeAndRound(Startup startup, EventType type, int round);
    List<Event> findByBattleId(Long battleId);
    boolean existsByStartupAndRound(Startup startup, int round);


}
