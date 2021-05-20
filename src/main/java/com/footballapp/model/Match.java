package com.footballapp.model;

public class Match {

    private TeamDetails team1;
    private TeamDetails team2;
    private String locationName;
    private TeamDetails homeTeam;

    public String getLocationName() {
        return this.locationName;
    }

    public TeamDetails getHomeTeam() {
        return this.homeTeam;
    }

    public void setTeam1(TeamDetails team1) {
        this.team1 = team1;
    }

    public void setTeam2(TeamDetails team2) {
        this.team2 = team2;
    }

    public TeamDetails getTeam1() {
        return team1;
    }

    public TeamDetails getTeam2() {
        return team2;
    }

    public void setHomeTeam(TeamDetails homeTeam) {
        this.homeTeam = homeTeam;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
}
