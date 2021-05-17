package com.footballapp.configuration;

import com.footballapp.model.TeamDetails;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class FileloadTest {

    private List<TeamDetails> teamDetailsList = new ArrayList<>();

    @InjectMocks
    private FileDataload fileDataload;

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
    public void testGenerateFinalScheduleByDate() throws ParseException, IOException {
        List<TeamDetails> teamDetailsLoaded = fileDataload.getTeamDetails();
        assertEquals(teamDetailsLoaded.size(), 10);
    }

}
