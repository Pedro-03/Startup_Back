package com.dell.startup.dto;

public class EventDTO {
    private Long battleId;
    private Long startupId;
    private String startup;
    private String event;
    private int pointsBefore;
    private int pointsAfter;

    public EventDTO(Long battleId, Long startupId, String startup, String event, int pointsBefore, int pointsAfter) {
        this.battleId = battleId;
        this.startupId = startupId;
        this.startup = startup;
        this.event = event;
        this.pointsBefore = pointsBefore;
        this.pointsAfter = pointsAfter;
    }

    public Long getBattleId() {
        return battleId;
    }

    public Long getStartupId() {
        return startupId;
    }

    public String getStartup() {
        return startup;
    }

    public String getEvent() {
        return event;
    }

    public int getPointsBefore() {
        return pointsBefore;
    }

    public int getPointsAfter() {
        return pointsAfter;
    }
}
