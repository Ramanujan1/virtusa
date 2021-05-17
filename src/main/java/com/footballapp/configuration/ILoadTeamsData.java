package com.footballapp.configuration;

import com.footballapp.model.TeamDetails;

import java.io.IOException;
import java.util.List;

public interface ILoadTeamsData {

    List<TeamDetails>  getTeamDetails() throws IOException;
}
