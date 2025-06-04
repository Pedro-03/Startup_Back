package com.dell.startup.services;

import com.dell.startup.dto.EventDTO;
import com.dell.startup.models.*;
import com.dell.startup.repositories.*;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private StartupRepository startupRepository;

    @Autowired
    private BattleRepository battleRepository;

    public Event applyEvent(Long startupId, Long battleId, EventType type) {
        Startup startup = startupRepository.findById(startupId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Startup not found"));

        Battle battle = battleRepository.findById(battleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Battle not found"));

        int round = battle.getRound();

        boolean alreadyApplied = eventRepository.existsByStartupAndTypeAndRound(startup, type, round);
        if (alreadyApplied) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "This event has already been applied to this startup in this round.");
        }

        startup.setPoints(startup.getPoints() + type.getPoints());
        startupRepository.save(startup);

        Event event = new Event(startup, battle, type, round);
        return eventRepository.save(event);
    }

    public List<Event> getEventsByBattle(Long battleId) {
        return eventRepository.findByBattleId(battleId);
    }

    public void applyRandomEventsToAllUnfinishedBattles() {
        List<Battle> battles = battleRepository.findAll();

        for (Battle battle : battles) {
            if (!battle.isFinished()) {
                List<Startup> startups = List.of(battle.getStartupA(), battle.getStartupB());

                for (Startup startup : startups) {
                    int round = battle.getRound();
                    if (eventRepository.existsByStartupAndRound(startup, round)) {
                        continue;
                    }
                    EventType[] types = EventType.values();
                    EventType type = types[new Random().nextInt(types.length)];

                    startup.setPoints(startup.getPoints() + type.getPoints());
                    startupRepository.save(startup);

                    System.out.println("Saving event: startup=" + startup.getName() + ", round=" + round + ", type=" + type);

                    Event event = new Event(startup, battle, type, round);
                    eventRepository.save(event);
                }
            }
        }
    }

    public List<EventDTO> getSimplifiedEventsByBattle(Long battleId) {
        List<Event> events = eventRepository.findByBattleId(battleId);

        List<EventDTO> simplified = new ArrayList<>();
        for (Event event : events) {
            Startup startup = event.getStartup();
            int pointsAfter = startup.getPoints();
            int pointsBefore = pointsAfter - event.getType().getPoints();

            simplified.add(new EventDTO(
                    event.getBattle().getId(),
                    startup.getId(),
                    startup.getName(),
                    event.getType().name(),
                    pointsBefore,
                    pointsAfter
            ));
        }
        return simplified;
    }

    public boolean isBattleReadyForFinalization(Battle battle) {
        Startup a = battle.getStartupA();
        Startup b = battle.getStartupB();
        int round = battle.getRound();

        return eventRepository.existsByStartupAndRound(a, round) ||
               eventRepository.existsByStartupAndRound(b, round);
    }

    public List<Map<String, Object>> getPointsPerRound() {
        List<Event> events = eventRepository.findAll();

        Map<Integer, List<Event>> byRound = new HashMap<>();
        for (Event e : events) {
            int round = e.getRound();
            byRound.computeIfAbsent(round, r -> new ArrayList<>()).add(e);
        }

        List<Integer> rounds = new ArrayList<>(byRound.keySet());
        Collections.sort(rounds);

        List<Map<String, Object>> result = new ArrayList<>();

        for (int round : rounds) {
            Map<Long, Integer> roundPoints = new HashMap<>();
            Map<Long, String> names = new HashMap<>();

            for (Event e : byRound.get(round)) {
                Long id = e.getStartup().getId();
                names.putIfAbsent(id, e.getStartup().getName());
                roundPoints.put(id, roundPoints.getOrDefault(id, 0) + e.getType().getPoints());
            }

            List<Map<String, Object>> startups = new ArrayList<>();
            for (Long id : roundPoints.keySet()) {
                Map<String, Object> s = new HashMap<>();
                s.put("id", id);
                s.put("name", names.get(id));
                s.put("points", roundPoints.get(id));
                startups.add(s);
            }

            startups.sort((a, b) -> ((Integer) b.get("points")).compareTo((Integer) a.get("points")));

            Map<String, Object> roundData = new HashMap<>();
            roundData.put("round", round);
            roundData.put("startups", startups);
            result.add(roundData);
        }

        return result;
    }
}
