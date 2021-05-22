package com.footballapp.scheduler;

import com.footballapp.model.FinalSchedule;
import com.footballapp.model.Match;
import com.footballapp.model.TeamDetails;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

import static com.footballapp.utils.Constants.VERSES;
import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;

@Service
public class ScheduleComputationService {

    @Inject
    ISortScedule sortSchedule;

    @Inject
    @Qualifier("teamDetailsList")
    private List<TeamDetails> teamDetailsList;
    List<String> matchScheduleSorted = new LinkedList<>();

    private List<String> generateMatchListForFinalSchedule(List<TeamDetails> teamDetailsList) {

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

        return sortSchedule.handleUnsortedHomeAwayMatches(matchScheduleSorted, matchCombinationsStringFromInput);

    }


    public FinalSchedule generateFinalScheduleByDate(Date scheduleDate) {
        Map<String, TeamDetails> teamDetailsMap = teamDetailsList.stream()
                .collect(Collectors.toMap(TeamDetails::getTeamName, teamDetails -> teamDetails));

        FinalSchedule finalSchedule = new FinalSchedule();

        List<String> sortedSchedule = generateMatchListForFinalSchedule(teamDetailsList);
        List<Match> matchList = new ArrayList<>();

        sortedSchedule.stream().forEach(matchString -> {
            String[] teamsPlaying = matchString.split(VERSES);
            Match match = new Match();
            match.setTeam1(teamDetailsMap.get(teamsPlaying[0]));
            match.setHomeTeam(teamDetailsMap.get(teamsPlaying[0]));
            match.setTeam2(teamDetailsMap.get(teamsPlaying[1]));
            match.setLocationName(match.getTeam1().getLocationName());

            matchList.add(match);
            finalSchedule.setMatchList(matchList);

        });

        finalSchedule.setScheduleDate(scheduleDate);

        LOGGER.info("Football Scheduler : Final Schedule generate for the given date");

        return finalSchedule;
    }

    public void setTeamDetailsList(List<TeamDetails> teamDetailsList) {
        this.teamDetailsList = teamDetailsList;
    }

    public void setSortScedule(ISortScedule sortSchedule) {
        this.sortSchedule = sortSchedule;
    }

}

