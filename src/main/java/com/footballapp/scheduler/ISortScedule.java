package com.footballapp.scheduler;

import com.footballapp.exception.NoTeamDetailsFound;
import com.footballapp.model.TeamDetails;

import java.util.List;

public interface ISortScedule {

    List<String> generateMatchListForFinalSchedule(List<TeamDetails> teamDetailsList) throws NoTeamDetailsFound;
    List<String> handleUnsortedHomeAwayMatches(List matchScheduleSorted, List matchCombinationsStringFromInput);

}
