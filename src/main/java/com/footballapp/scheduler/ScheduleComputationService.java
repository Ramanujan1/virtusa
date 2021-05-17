package com.footballapp.scheduler;

import com.footballapp.model.FinalSchedule;
import com.footballapp.model.Match;
import com.footballapp.model.TeamDetails;
import com.footballapp.utils.Constants;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

import static com.footballapp.utils.Constants.Verces;

@Service
public class ScheduleComputationService {

    @Inject
    @Qualifier("teamDetailsList")
    private List<TeamDetails> teamDetailsList;

    private List<String> generateMatchListForFinalSchedule(List<TeamDetails> teamDetailsList) {

        HashMap teamLastPlayedLocationStatus = new HashMap();
        List<String> matchScheduleSorted = new LinkedList<>();

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
                                    return match.getTeam1().getTeamName().concat(Constants.Verces).concat(match.getTeam2().getTeamName());
                                }
                        )
                        .collect(Collectors.toList());

        return handleUnsortedHomeAwayMatches(matchScheduleSorted, teamLastPlayedLocationStatus, matchCombinationsStringFromInput);

    }

    private void initializeStausMap(HashMap teamLastPlayedLocationStatus, String[] teamArray) {
        if (teamLastPlayedLocationStatus.get(teamArray[0]) == null) {
            teamLastPlayedLocationStatus.put(teamArray[0], "");
        }

        if (teamLastPlayedLocationStatus.get(teamArray[1]) == null) {
            teamLastPlayedLocationStatus.put(teamArray[1], "");
        }
    }


    private List<String> handleUnsortedHomeAwayMatches(List<String> matchScheduleSorted, HashMap teamLastPlayedLocationStatus, List<String> matchCombinationsStringFromInput) {
        // for the remaining unscheduled matches iterate multiple times to insert into the final sorted schedule

        int unsortedListIterationCount = 0;
        while (matchCombinationsStringFromInput.size() > 0) {
            Iterator unsortedListIterator = matchCombinationsStringFromInput.iterator();

            while (unsortedListIterator.hasNext()) {

                unsortedListIterationCount++;

                if (unsortedListIterationCount > 2) {
                    // handle special case where the top most match combination in the unsorted list cannot be scheduled
                    // in such cases swap the top most element with the last element and continue the iteration
                    String lastMatch = matchCombinationsStringFromInput.get(matchCombinationsStringFromInput.size() - 1);
                    matchCombinationsStringFromInput.set(matchCombinationsStringFromInput.size() - 1, matchCombinationsStringFromInput.get(0));
                    matchCombinationsStringFromInput.set(0, lastMatch);
                }

                String team = (String) unsortedListIterator.next();
                String[] teamArray = team.split(Verces);


                initializeStausMap(teamLastPlayedLocationStatus, teamArray);

                unsortedListIterationCount = getUnsortedListIterationCount(matchScheduleSorted, teamLastPlayedLocationStatus, unsortedListIterationCount, unsortedListIterator, teamArray);


            }
        }
        return matchScheduleSorted;
    }

    private int getUnsortedListIterationCount(List<String> matchScheduleSorted, HashMap teamLastPlayedLocationStatus, int unsortedListIterationCount, Iterator unsortedListIterator, String[] teamArray) {
        if ((teamLastPlayedLocationStatus.get(teamArray[0]).equals(Constants.MatchLocation.HOME)
                && teamLastPlayedLocationStatus.get(teamArray[1]).equals(Constants.MatchLocation.HOME))
                || (
                teamLastPlayedLocationStatus.get(teamArray[0]).equals(Constants.MatchLocation.AWAY)
                        && teamLastPlayedLocationStatus.get(teamArray[1]).equals(Constants.MatchLocation.AWAY)
        )) {
            if (unsortedListIterationCount > 5) {
                addToSortedList(matchScheduleSorted, teamLastPlayedLocationStatus, unsortedListIterator, teamArray[1], teamArray[0]);

                unsortedListIterationCount = 0;
            }

        } else if ((teamLastPlayedLocationStatus.get(teamArray[0]).equals(Constants.MatchLocation.HOME)
                && teamLastPlayedLocationStatus.get(teamArray[1]).equals(Constants.MatchLocation.AWAY))

                ||
                (teamLastPlayedLocationStatus.get(teamArray[0]).equals(Constants.MatchLocation.HOME)
                )
                || (teamLastPlayedLocationStatus.get(teamArray[1]).equals(Constants.MatchLocation.AWAY))

                ) {

            addToSortedList(matchScheduleSorted, teamLastPlayedLocationStatus, unsortedListIterator, teamArray[0], teamArray[1]);

            unsortedListIterationCount = 0;

        } else {

            addToSortedList(matchScheduleSorted, teamLastPlayedLocationStatus, unsortedListIterator, teamArray[1], teamArray[0]);

            unsortedListIterationCount = 0;
        }
        return unsortedListIterationCount;
    }


    private void addToSortedList(List<String> matchScheduleSorted, HashMap teamLastPlayedLocationStatus, Iterator iter, String teamHome, String teamAway) {
        matchScheduleSorted.add(teamAway.concat(Constants.Verces).concat(teamHome));
        iter.remove();

        teamLastPlayedLocationStatus.put(teamHome, Constants.MatchLocation.AWAY);
        teamLastPlayedLocationStatus.put(teamAway, Constants.MatchLocation.HOME);
    }

    public FinalSchedule generateFinalScheduleByDate(Date scheduleDate) {
        Map<String, TeamDetails> teamDetailsMap = teamDetailsList.stream()
                .collect(Collectors.toMap(TeamDetails::getTeamName, teamDetails -> teamDetails));

        FinalSchedule finalSchedule = new FinalSchedule();

        List<String> sortedSchedule = generateMatchListForFinalSchedule(teamDetailsList);
        List<Match> matchList = new ArrayList<>();

        sortedSchedule.stream().forEach(matchString -> {
            String[] teamsPlaying = matchString.split(Verces);
            Match match = new Match();
            match.setTeam1(teamDetailsMap.get(teamsPlaying[0]));
            match.setHomeTeam(teamDetailsMap.get(teamsPlaying[0]));
            match.setTeam2(teamDetailsMap.get(teamsPlaying[1]));
            match.setLocationName(match.getTeam1().getLocationName());

            matchList.add(match);

            finalSchedule.setMatchList(matchList);

        });
        finalSchedule.setScheduleDate(scheduleDate);

        return finalSchedule;
    }

    public void setTeamDetailsList(List<TeamDetails> teamDetailsList) {
        this.teamDetailsList = teamDetailsList;
    }
}

