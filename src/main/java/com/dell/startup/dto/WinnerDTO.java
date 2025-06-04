package com.dell.startup.dto;

public class WinnerDTO {
    private Long id;
    private String name;
    private String slogan;

    public WinnerDTO(Long id, String name, String slogan) {
        this.id = id;
        this.name = name;
        this.slogan = slogan;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSlogan() {
        return slogan;
    }
}
