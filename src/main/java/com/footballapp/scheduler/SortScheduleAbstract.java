package com.footballapp.scheduler;

import com.footballapp.utils.Constants;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.footballapp.utils.Constants.VERSES;

public  abstract class SortScheduleAbstract {

    protected void addToSortedList(List<String> matchScheduleSorted, Map teamLastPlayedLocationStatus, String teamHome, String teamAway) {
        matchScheduleSorted.add(teamAway.concat(VERSES).concat(teamHome));

        teamLastPlayedLocationStatus.put(teamHome, Constants.MatchLocation.AWAY);
        teamLastPlayedLocationStatus.put(teamAway, Constants.MatchLocation.HOME);
    }

    protected void reconcileSortedAndUnsortedList(List<String> matchScheduleSorted, Map teamLastPlayedLocationStatus, Iterator unsortedListIterator, String team1, String team2) {

        addToSortedList(matchScheduleSorted, teamLastPlayedLocationStatus, team1, team2);
        unsortedListIterator.remove();
    }
}
