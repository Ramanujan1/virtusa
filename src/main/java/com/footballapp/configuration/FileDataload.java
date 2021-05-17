package com.footballapp.configuration;

import com.footballapp.model.TeamDetails;
import com.footballapp.utils.Constants;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileDataload implements ILoadTeamsData{

    BufferedReader br;

    public List<TeamDetails> getTeamDetails() throws IOException {

        br = new BufferedReader(new FileReader(Constants.DATA_FILE));
        String line = "";
        List<TeamDetails> teamDetailsList = new ArrayList<>();

        while((line = br.readLine()) != null) {
            String[] fields = line.split(Constants.FILE_DELIMITER);
            TeamDetails teamDetails = new TeamDetails();
            teamDetails.setTeamName(fields[0]);
            teamDetails.setLocationName(fields[1]);
            teamDetailsList.add(teamDetails);
        }
        return teamDetailsList;
    }
}
