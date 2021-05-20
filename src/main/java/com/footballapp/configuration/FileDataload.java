package com.footballapp.configuration;

import com.footballapp.model.TeamDetails;
import com.footballapp.utils.Constants;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileDataload implements ILoadTeamsData{

    private BufferedReader bufferedReader;

    public List<TeamDetails> getTeamDetails() throws IOException {

        Scanner sc = new Scanner(new FileReader(Constants.DATA_FILE));
        String line = "";
        final List<TeamDetails> teamDetailsList = new ArrayList<>();

        while((sc.hasNextLine())) {
            String[] fields = sc.nextLine().split(Constants.FILE_DELIMITER);
            TeamDetails teamDetails = new TeamDetails();
            teamDetails.setTeamName(fields[0]);
            teamDetails.setLocationName(fields[1]);
            teamDetailsList.add(teamDetails);
        }
        return teamDetailsList;
    }
}
