package com.dell.startup.controllers;

import com.dell.startup.dto.EventDTO;
import com.dell.startup.models.*;
import com.dell.startup.services.EventService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping
    public Event applyEvent(@RequestParam Long startupId, @RequestParam Long battleId, @RequestParam EventType type) {
        return eventService.applyEvent(startupId, battleId, type);
    }

    @GetMapping("/battle/{battleId}")
    public List<Event> getEventsByBattle(@PathVariable Long battleId) {
        return eventService.getEventsByBattle(battleId);
    }

    @PostMapping("/random")
    public void applyRandomEventsToUnfinishedBattles() {
        eventService.applyRandomEventsToAllUnfinishedBattles();
    }

    // conferir, fazer com q mostre todos os eventos da rodada
    @GetMapping("/battle/{battleId}/summary")
    public List<EventDTO> getEventSummaryByBattle(@PathVariable Long battleId) {
        return eventService.getSimplifiedEventsByBattle(battleId);
    }

    @GetMapping("/types")
    public EventType[] getEventTypes() {
        return EventType.values();
    }

    @GetMapping("/scores")
    public List<Map<String, Object>> getRoundScores() {
        return eventService.getPointsPerRound();
    }

}
