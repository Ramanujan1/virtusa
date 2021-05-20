package com.footballapp.model;

public class TeamDetails {

    private String teamName;
    private String locationName;
    private String localStadiumName;
    private String locationId;

    public String getTeamName() {
        return teamName;
    }

    public String getLocationName() {
        return locationName;
    }

    public String getLocalStadiumName() {
        return localStadiumName;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public void setLocalStadiumName(String localStadiumName) {
        this.localStadiumName = localStadiumName;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }
}
