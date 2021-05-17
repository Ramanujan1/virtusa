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

    private String tournamentName;
    private List<Match> matches;


    private List<String> generateFinalSchedule(List<TeamDetails> teamDetailsList) {

        HashMap teamLastPlayedLocationStatus = new HashMap();
        List<String> matchScheduleSorted = new LinkedList<>();
        List<String> matchScheduleUnSorted = new LinkedList<>();

        // generate all possible match combinations for given team list
        Set<String> matchCombinationsString =
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
                        .collect(Collectors.toSet());

        return sortMatchSchedules(matchCombinationsString, matchScheduleUnSorted, teamLastPlayedLocationStatus, matchScheduleSorted);
    }


    private List<String> sortMatchSchedules(Set<String> matchCombinationsString, List<String> matchScheduleUnSorted, HashMap teamLastPlayedLocationStatus, List<String> matchScheduleSorted) {


        // for all match combination apply the home and away rule for ordering
        // the undorted list should be empty by the end of the iteration
        matchCombinationsString.stream().forEach(matchKey -> {
            String[] teamArray = matchKey.split(Verces);

            if (teamLastPlayedLocationStatus.get(teamArray[0]) == null) {
                teamLastPlayedLocationStatus.put(teamArray[0], "");
            }

            if (teamLastPlayedLocationStatus.get(teamArray[1]) == null) {
                teamLastPlayedLocationStatus.put(teamArray[1], "");
            }

            if ((teamLastPlayedLocationStatus.get(teamArray[0]).equals(Constants.MatchLocation.HOME)
                    && teamLastPlayedLocationStatus.get(teamArray[1]).equals(Constants.MatchLocation.HOME))
                    || (
                    teamLastPlayedLocationStatus.get(teamArray[0]).equals(Constants.MatchLocation.AWAY)
                            && teamLastPlayedLocationStatus.get(teamArray[1]).equals(Constants.MatchLocation.AWAY)
            )) {
                matchScheduleUnSorted.add(matchKey);

            } else if ((teamLastPlayedLocationStatus.get(teamArray[0]).equals(Constants.MatchLocation.HOME)
                    && teamLastPlayedLocationStatus.get(teamArray[1]).equals(Constants.MatchLocation.AWAY))

                    ||
                    (teamLastPlayedLocationStatus.get(teamArray[0]).equals(Constants.MatchLocation.HOME)
                    )
                    || (teamLastPlayedLocationStatus.get(teamArray[1]).equals(Constants.MatchLocation.AWAY))

                    ) {
                matchScheduleSorted.add(teamArray[1].concat(Verces).concat(teamArray[0]));

                teamLastPlayedLocationStatus.put(teamArray[0], Constants.MatchLocation.AWAY);
                teamLastPlayedLocationStatus.put(teamArray[1], Constants.MatchLocation.HOME);


            } else {
                matchScheduleSorted.add(matchKey);
                teamLastPlayedLocationStatus.put(teamArray[1], Constants.MatchLocation.AWAY);
                teamLastPlayedLocationStatus.put(teamArray[0], Constants.MatchLocation.HOME);
            }
        });


        return handleUnsortedMatches(matchScheduleSorted, teamLastPlayedLocationStatus, matchScheduleUnSorted);
    }


    private List<String> handleUnsortedMatches(List<String>  matchScheduleSorted , HashMap teamLastPlayedLocationStatus, List<String> matchScheduleUnSorted) {
        // for the remaining unscheduled matches iterate multiple times to insert into the final sorted schedule
        int interationCount = 0;
        while (matchScheduleUnSorted.size() > 0) {


            Iterator iter = matchScheduleUnSorted.iterator();

            while (iter.hasNext()) {
                interationCount++;
                if(interationCount > 2) {
                    // handle special case where
                    String lastMatch = matchScheduleUnSorted.get(matchScheduleUnSorted.size() - 1);
                    matchScheduleUnSorted.set(matchScheduleUnSorted.size() - 1, matchScheduleUnSorted.get(0));
                    matchScheduleUnSorted.set(0, lastMatch);
                }

                String team = (String) iter.next();
                String[] teamArray = team.split(Verces);


                if ((teamLastPlayedLocationStatus.get(teamArray[0]).equals(Constants.MatchLocation.HOME)
                        && teamLastPlayedLocationStatus.get(teamArray[1]).equals(Constants.MatchLocation.HOME))
                        || (
                        teamLastPlayedLocationStatus.get(teamArray[0]).equals(Constants.MatchLocation.AWAY)
                                && teamLastPlayedLocationStatus.get(teamArray[1]).equals(Constants.MatchLocation.AWAY)
                )) {
                    if(interationCount > 5) {
                        addToSortedList( matchScheduleSorted,  teamLastPlayedLocationStatus,  iter,  teamArray[1],  teamArray[0] );

                        interationCount=0;
                    }

                } else if ((teamLastPlayedLocationStatus.get(teamArray[0]).equals(Constants.MatchLocation.HOME)
                        && teamLastPlayedLocationStatus.get(teamArray[1]).equals(Constants.MatchLocation.AWAY))

                        ||
                        (teamLastPlayedLocationStatus.get(teamArray[0]).equals(Constants.MatchLocation.HOME)
                        )
                        || (teamLastPlayedLocationStatus.get(teamArray[1]).equals(Constants.MatchLocation.AWAY))

                        ) {
//                    matchScheduleSorted.add(teamArray[1].concat(Constants.Verces).concat(teamArray[0]));
//                    iter.remove();
//
//                    teamLastPlayedLocationStatus.put(teamArray[0], Constants.MatchLocation.AWAY);
//                    teamLastPlayedLocationStatus.put(teamArray[1], Constants.MatchLocation.HOME);

                    addToSortedList( matchScheduleSorted,  teamLastPlayedLocationStatus,  iter,  teamArray[0],  teamArray[1] );

                        interationCount=0;

                } else {
//                    matchScheduleSorted.add(team);
//                    iter.remove();
//                    teamLastPlayedLocationStatus.put(teamArray[1], Constants.MatchLocation.AWAY);
//                    teamLastPlayedLocationStatus.put(teamArray[0], Constants.MatchLocation.HOME);

                    addToSortedList( matchScheduleSorted,  teamLastPlayedLocationStatus,  iter,  teamArray[1],  teamArray[0] );

                    interationCount=0;
                }


            }
        }
        return matchScheduleSorted;
    }


    private void addToSortedList(List<String> matchScheduleSorted, HashMap teamLastPlayedLocationStatus, Iterator iter, String teamHome, String teamAway ) {
        matchScheduleSorted.add(teamAway.concat(Constants.Verces).concat(teamHome));
        iter.remove();

        teamLastPlayedLocationStatus.put(teamHome, Constants.MatchLocation.AWAY);
        teamLastPlayedLocationStatus.put(teamAway, Constants.MatchLocation.HOME);
    }

    public FinalSchedule generateFinalScheduleByDate(Date scheduleDate) {
        Map<String, TeamDetails> teamDetailsMap = teamDetailsList.stream()
                .collect(Collectors.toMap(TeamDetails::getTeamName, teamDetails -> teamDetails));

        FinalSchedule finalSchedule = new FinalSchedule();

        List<String> sortedSchedule = generateFinalSchedule(teamDetailsList);
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

