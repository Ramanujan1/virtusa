package com.footballapp.componenttest;

import com.footballapp.controller.ScheduleController;
import com.footballapp.model.FinalSchedule;
import com.footballapp.model.Match;
import com.footballapp.model.TeamDetails;
import com.footballapp.scheduler.ScheduleComputationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@WebMvcTest(ScheduleController.class)
public class ComponentTest {

    FinalSchedule finalSchedule;
    List<Match> matchList;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ScheduleComputationService scheduleComputationService;

    @Before
    public void setUp() {

        matchList = new ArrayList<>();
        finalSchedule = new FinalSchedule();
        Match match = new Match();

        TeamDetails teamDetails = new TeamDetails();
        teamDetails.setTeamName("Team1");
        teamDetails.setLocationName("Location1");

        match.setTeam1(teamDetails);
        match.setHomeTeam(teamDetails);

        teamDetails = new TeamDetails();
        teamDetails.setTeamName("Team2");
        teamDetails.setLocationName("Location2");

        match.setTeam2(teamDetails);


        matchList.add(match);

        Match match2 = new Match();

        teamDetails = new TeamDetails();
        teamDetails.setTeamName("Team2");
        teamDetails.setLocationName("Location2");

        match2.setTeam1(teamDetails);
        match2.setHomeTeam(teamDetails);

        teamDetails = new TeamDetails();
        teamDetails.setTeamName("Team1");
        teamDetails.setLocationName("Location1");

        match2.setTeam2(teamDetails);

        matchList.add(match2);

        finalSchedule.setMatchList(matchList);
    }

    @Test
    public void shouldInvokeCreateScheduleSuccessfully() throws Exception {

        this.mockMvc
                .perform(get("/createSchedule"))
                .andExpect(status().isOk())
                .andExpect(view().name("matchSchedule"))
                .andExpect(model().size(0));
    }


    @Test
    public void shouldReturnViewWithPrefilledData() throws Exception {

        Date scheduleDate = new SimpleDateFormat("yyyy-mm-dd").parse("2021-05-17");
        when(scheduleComputationService.generateFinalScheduleByDate(scheduleDate)).thenReturn(finalSchedule);
        this.mockMvc
                .perform(post("/generateSchedule?scheduleDate=2021-05-17")
                        .contentType(MediaType.TEXT_PLAIN)
                )

                .andExpect(status().isOk())
                .andExpect(view().name("matchSchedule"))
                .andExpect(model().attribute("matches", scheduleComputationService.generateFinalScheduleByDate(scheduleDate).getMatchList()))
                .andExpect(model().attribute("scheduleDateFinal", "17 January 2021"))
                .andExpect(model().size(2));
    }


    @Test
    public void shouldReturnError() throws Exception {


        this.mockMvc
                .perform(post("/generateSchedule?scheduleDate=")
                        .contentType(MediaType.TEXT_PLAIN)
                )

                .andExpect(status().isOk())
                .andExpect(view().name("matchSchedule"))
                .andExpect(model().attribute("error", "Enter a valid date"))
                .andExpect(model().size(1));
    }
}