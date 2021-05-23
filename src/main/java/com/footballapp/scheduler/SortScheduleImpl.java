package com.footballapp.scheduler;

import com.footballapp.exception.NoTeamDetailsFound;
import com.footballapp.model.Match;
import com.footballapp.model.TeamDetails;
import com.footballapp.utils.Constants;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.footballapp.utils.Constants.VERSES;
import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;

public class SortScheduleImpl extends SortScheduleAbstract implements ISortScedule {

    Map teamLastPlayedLocationStatus = new ConcurrentHashMap();
    List<String> matchScheduleSorted = new LinkedList<>();

    @Override
    public List<String> generateMatchListForFinalSchedule(List<TeamDetails> teamDetailsList) throws NoTeamDetailsFound {

        if(teamDetailsList == null || teamDetailsList.size() == 0 ) {
            LOGGER.info("Football Scheduler :  No Team Details available to process !!!");
            throw new NoTeamDetailsFound(" No Team Details available to process !!!");
        }
        // generate all possible match combinations for given team list
        List<String> matchCombinationsStringFromInput =
                teamDetailsList.stream()
                        .flatMap(homeTeam -> teamDetailsList.stream().map(awayTeam -> {
                            Match match = new Match();
                            match.setTeam1(homeTeam);
                            match.setTeam2(awayTeam);
                            match.setHomeTeam(homeTeam);
                            return match;
                        })).filter(match -> match != null && !match.getTeam1().equals(match.getTeam2()))
                        .map(match -> {
                                    return match.getTeam1().getTeamName().concat(VERSES).concat(match.getTeam2().getTeamName());
                                }
                        )
                        .collect(Collectors.toList());

        LOGGER.info("Football Scheduler : Generated All possible Match combinations for team list, Match combinations count: "+matchCombinationsStringFromInput.size());

        return handleUnsortedHomeAwayMatches(matchScheduleSorted, matchCombinationsStringFromInput);

    }

    @Override
    public synchronized List<String> handleUnsortedHomeAwayMatches(List matchScheduleSorted, List matchCombinationsStringFromInput) {

        if(matchCombinationsStringFromInput == null || matchCombinationsStringFromInput.size() == 0 ) {
            LOGGER.info("Football Scheduler :  No Team Details available to process !!!");
            throw new NoTeamDetailsFound(" No Team Details available to process !!!");
        }

        int unsortedListIterationCount = 0;
        while (!matchCombinationsStringFromInput.isEmpty()) {
            Iterator unsortedListIterator = matchCombinationsStringFromInput.iterator();

            while (unsortedListIterator.hasNext()) {

                unsortedListIterationCount++;

                if (unsortedListIterationCount > 2) {
                    // handle special case where the top most match combination in the unsorted list cannot be scheduled due to the home away rule
                    // in such cases swap the top most element with the last element and continue the iteration
                    String lastMatch = (String) matchCombinationsStringFromInput.get(matchCombinationsStringFromInput.size() - 1);
                    matchCombinationsStringFromInput.set(matchCombinationsStringFromInput.size() - 1, matchCombinationsStringFromInput.get(0));
                    matchCombinationsStringFromInput.set(0, lastMatch);
                }

                String team = (String) unsortedListIterator.next();
                String[] teamArray = team.split(VERSES);

                initializeStausMap(teamLastPlayedLocationStatus, teamArray);

                unsortedListIterationCount = getUnsortedListIterationCount(matchScheduleSorted, teamLastPlayedLocationStatus, unsortedListIterationCount, unsortedListIterator, matchCombinationsStringFromInput.size(), teamArray);
            }
        }

        LOGGER.info("Football Scheduler : Generated sorted schedule from Match combinations , Final sorted list count: " + matchScheduleSorted.size());
        return matchScheduleSorted;
    }

    private void initializeStausMap(Map teamLastPlayedLocationStatus, String... teamArray) {
        if (teamLastPlayedLocationStatus.get(teamArray[0]) == null) {
            teamLastPlayedLocationStatus.put(teamArray[0], "");
        }

        if (teamLastPlayedLocationStatus.get(teamArray[1]) == null) {
            teamLastPlayedLocationStatus.put(teamArray[1], "");
        }
        LOGGER.info("Football Scheduler : Generated sorted schedule from Match combinations , Initialize teamLastPlayedLocationStatus Map");
    }

    //check te hoome and away rule for the match in question
    private int getUnsortedListIterationCount(List<String> matchScheduleSorted, Map teamLastPlayedLocationStatus, int unsortedListIterationCount, Iterator unsortedListIterator, int unsortedListSize, String... teamArray) {
        if ((teamLastPlayedLocationStatus.get(teamArray[0]).equals(Constants.MatchLocation.HOME)
                && teamLastPlayedLocationStatus.get(teamArray[1]).equals(Constants.MatchLocation.HOME))
                || (
                teamLastPlayedLocationStatus.get(teamArray[0]).equals(Constants.MatchLocation.AWAY)
                        && teamLastPlayedLocationStatus.get(teamArray[1]).equals(Constants.MatchLocation.AWAY)
        )) {
            // this is to handle the condition where we have tried swapping the topmost element with the bottom most
            // and still could not schedule the match for
            if (unsortedListIterationCount > unsortedListSize + 1) {
                LOGGER.info("Football Scheduler : Could not schedule this match according to the Home away rule, Adding to this schedule list");
                reconcileSortedAndUnsortedList(matchScheduleSorted, teamLastPlayedLocationStatus, unsortedListIterator, teamArray[1], teamArray[0]);
                unsortedListIterationCount = 0;
            }

        } else if ((teamLastPlayedLocationStatus.get(teamArray[0]).equals(Constants.MatchLocation.HOME)
                && teamLastPlayedLocationStatus.get(teamArray[1]).equals(Constants.MatchLocation.AWAY))
                ||
                (teamLastPlayedLocationStatus.get(teamArray[0]).equals(Constants.MatchLocation.HOME)
                )
                || (teamLastPlayedLocationStatus.get(teamArray[1]).equals(Constants.MatchLocation.AWAY))

                ) {

            reconcileSortedAndUnsortedList(matchScheduleSorted, teamLastPlayedLocationStatus, unsortedListIterator, teamArray[0], teamArray[1]);
            unsortedListIterationCount = 0;

        } else {

            reconcileSortedAndUnsortedList(matchScheduleSorted, teamLastPlayedLocationStatus, unsortedListIterator, teamArray[1], teamArray[0]);
            unsortedListIterationCount = 0;
        }

        LOGGER.info("Football Scheduler : Generating unsorted schedule from Match combinations ");

        return unsortedListIterationCount;
    }
}
