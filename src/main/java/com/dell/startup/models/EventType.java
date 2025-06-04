package com.dell.startup.models;

public enum EventType {

    CONVINCING_PITCH(6),
    BUGGY_PRODUCT(-4),
    GOOD_USER_TRACTION(3),
    ANGRY_INVESTOR(-6),
    FAKE_NEWS_IN_PITCH(-8);

    private final int points;

    EventType(int points) {
        this.points = points;
    }

    public int getPoints() {
        return points;
    }
}
