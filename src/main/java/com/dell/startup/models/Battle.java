package com.dell.startup.models;

import jakarta.persistence.*;

@Entity
public class Battle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Startup startupA;

    @ManyToOne
    private Startup startupB;

    @ManyToOne
    private Startup winner;

    @Column(nullable = false)
    private boolean finished;
    @Column(nullable = false)
    private int round;

    public Battle() {}

    public Battle(Startup startupA, Startup startupB, int round) {
        this.startupA = startupA;
        this.startupB = startupB;
        this.round = round;
        this.finished = false;
    }

    public Long getId() {
        return id;
    }

    public Startup getStartupA() {
        return startupA;
    }

    public void setStartupA(Startup startupA) {
        this.startupA = startupA;
    }

    public Startup getStartupB() {
        return startupB;
    }

    public void setStartupB(Startup startupB) {
        this.startupB = startupB;
    }

    public Startup getWinner() {
        return winner;
    }

    public void setWinner(Startup winner) {
        this.winner = winner;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }
}
