package com.dell.startup.dto;

public class StartupStatsDTO {

    private Long id;
    private String name;
    private int points;

    private long pitchCount;
    private long bugCount;
    private long tractionCount;
    private long angryInvestorCount;
    private long fakeNews;

    public StartupStatsDTO(Long id,String name,int points,long pitchCount,long bugCount,long tractionCount,long angryInvestorCount,long fakeNews) {
        this.id = id;
        this.name = name;
        this.points = points;
        this.pitchCount = pitchCount;
        this.bugCount = bugCount;
        this.tractionCount = tractionCount;
        this.angryInvestorCount = angryInvestorCount;
        this.fakeNews = fakeNews;
    }

    public Long getId() { 
        return id; 
    }

    public String getName() { 
        return name; 
    }

    public int getPoints() { 
        return points; 
    }
    public long getPitchCount() { 
        return pitchCount; 
    }

    public long getBugCount() { 
        return bugCount; 
    }

    public long getTractionCount() { 
        return tractionCount; 
    }

    public long getAngryInvestorCount() { 
        return angryInvestorCount; 
    }
    
    public long getfakeNews() { 
        return fakeNews; 
    }
}
