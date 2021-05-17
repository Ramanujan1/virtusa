package com.footballapp.scheduler;

import com.footballapp.model.FinalSchedule;
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
        for ( int i=0 ; i < 10; i++ ) {
            TeamDetails teamDetails = new TeamDetails();
            teamDetails.setTeamName("Team "+i);
            teamDetailsList.add(teamDetails);
        }
    }

    @Test
    public void testGenerateFinalScheduleByDate() throws ParseException {
        scheduleComputationService.setTeamDetailsList(teamDetailsList);
        FinalSchedule finalSchedule = scheduleComputationService.generateFinalScheduleByDate(new SimpleDateFormat("yyyy-mm-dd").parse("2021-05-17"));
        assertEquals(finalSchedule.getMatchList().size(), 90);
    }

    @Test
    public void testGenerateEmptySchedule() throws ParseException {
        scheduleComputationService.setTeamDetailsList(new ArrayList<>());
        FinalSchedule finalSchedule = scheduleComputationService.generateFinalScheduleByDate(new SimpleDateFormat("yyyy-mm-dd").parse("2021-05-17"));
        assertEquals(finalSchedule.getMatchList(), null);
    }
}
