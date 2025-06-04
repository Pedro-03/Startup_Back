package com.dell.startup.models;

import jakarta.persistence.*;

@Entity
public class Startup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String slogan;
    private int foundationDate;
    private int points;

    public Startup() {
        points = 70;
    }

    public Startup(String name, String slogan, int foundationDate) {
        this.name = name;
        this.slogan = slogan;
        this.foundationDate = foundationDate;
        points = 70;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public int getFoundationDate() {
        return foundationDate;
    }

    public void setFoundationDate(int foundationDate) {
        this.foundationDate = foundationDate;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
