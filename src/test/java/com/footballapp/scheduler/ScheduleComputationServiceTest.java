package com.footballapp.scheduler;

import com.footballapp.model.FinalSchedule;
import com.footballapp.model.Match;
import com.footballapp.model.TeamDetails;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class ScheduleComputationServiceTest {

    private List<TeamDetails> teamDetailsList = new ArrayList<>();

    @InjectMocks
    private ScheduleComputationService scheduleComputationService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        for (int i = 0; i < 10; i++) {
            TeamDetails teamDetails = new TeamDetails();
            teamDetails.setTeamName("Team " + i);
            teamDetailsList.add(teamDetails);
        }
    }

    @Test
    public void testGenerateFinalScheduleByDate() throws ParseException {
        scheduleComputationService.setTeamDetailsList(teamDetailsList);
        FinalSchedule finalSchedule = scheduleComputationService.generateFinalScheduleByDate(new SimpleDateFormat("yyyy-mm-dd").parse("2021-05-17"));

        assertEquals(finalSchedule.getMatchList().get(10).getTeam1().getTeamName(), "Team 3");
        assertEquals(finalSchedule.getMatchList().get(10).getTeam2().getTeamName(), "Team 1");
        assertEquals(finalSchedule.getMatchList().get(20).getTeam1().getTeamName(), "Team 2");
        assertEquals(finalSchedule.getMatchList().get(20).getTeam2().getTeamName(), "Team 5");
        assertEquals(finalSchedule.getMatchList().get(30).getTeam1().getTeamName(), "Team 7");
        assertEquals(finalSchedule.getMatchList().get(30).getTeam2().getTeamName(), "Team 3");
        assertEquals(finalSchedule.getMatchList().get(40).getTeam1().getTeamName(), "Team 4");
        assertEquals(finalSchedule.getMatchList().get(40).getTeam2().getTeamName(), "Team 9");
        assertEquals(finalSchedule.getMatchList().get(50).getTeam1().getTeamName(), "Team 6");
        assertEquals(finalSchedule.getMatchList().get(50).getTeam2().getTeamName(), "Team 1");
        assertEquals(finalSchedule.getMatchList().get(60).getTeam1().getTeamName(), "Team 3");
        assertEquals(finalSchedule.getMatchList().get(60).getTeam2().getTeamName(), "Team 7");
        assertEquals(finalSchedule.getMatchList().get(70).getTeam1().getTeamName(), "Team 8");
        assertEquals(finalSchedule.getMatchList().get(70).getTeam2().getTeamName(), "Team 5");
        assertEquals(finalSchedule.getMatchList().get(80).getTeam1().getTeamName(), "Team 7");
        assertEquals(finalSchedule.getMatchList().get(80).getTeam2().getTeamName(), "Team 9");
        assertEquals(finalSchedule.getMatchList().get(89).getTeam1().getTeamName(), "Team 7");
        assertEquals(finalSchedule.getMatchList().get(89).getTeam2().getTeamName(), "Team 8");

        assertEquals(finalSchedule.getMatchList().size(), 90);
    }

    @Test
    public void testGenerateEmptySchedule() throws ParseException {
        scheduleComputationService.setTeamDetailsList(new ArrayList<>());
        FinalSchedule finalSchedule = scheduleComputationService.generateFinalScheduleByDate(new SimpleDateFormat("yyyy-mm-dd").parse("2021-05-17"));
        assertEquals(finalSchedule.getMatchList(), null);
    }

    @Test
    public void testRemoveMatchComnnationsWithSameTeam() throws ParseException {
        scheduleComputationService.setTeamDetailsList(teamDetailsList);
        FinalSchedule finalSchedule = scheduleComputationService.generateFinalScheduleByDate(new SimpleDateFormat("yyyy-mm-dd").parse("2021-05-17"));
        List<Match> matchList = finalSchedule.getMatchList();

        boolean mamatchAgainstSameTeamFound = matchList.stream().anyMatch(match ->
                match.getTeam1().getTeamName().equals(match.getTeam2().getTeamName())
        );
        assertEquals(mamatchAgainstSameTeamFound, false);
    }
}
