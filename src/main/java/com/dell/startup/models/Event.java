package com.dell.startup.models;

import jakarta.persistence.*;

@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Startup startup;

    @ManyToOne
    private Battle battle;

    @Enumerated(EnumType.STRING)
    private EventType type;

    @Column(nullable = false)
    private int round;

    public Event() {}

    public Event(Startup startup, Battle battle, EventType type, int round) {
        this.startup = startup;
        this.battle = battle;
        this.type = type;
        this.round = round;
    }

    public Long getId() {
        return id;
    }

    public Startup getStartup() {
        return startup;
    }

    public void setStartup(Startup startup) {
        this.startup = startup;
    }

    public Battle getBattle() {
        return battle;
    }

    public void setBattle(Battle battle) {
        this.battle = battle;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }
}
