package com.footballapp.model;



import java.sql.Timestamp;
import java.util.Date;

public class Match {

    private TeamDetails team1;
    private TeamDetails team2;
    private String locationName;
    private String locationId;
    private Date matchDate;
    private TeamDetails homeTeam;
    private Timestamp matchTime;
    private int matchNumber;

    public int getMatchNumber() {
        return matchNumber;
    }

    public void setMatchNumber(int matchNumber) {
        this.matchNumber = matchNumber;
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

    public TeamDetails getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(TeamDetails homeTeam) {
        this.homeTeam = homeTeam;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
}
