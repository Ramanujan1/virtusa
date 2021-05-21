package com.footballapp.scheduler;

import com.footballapp.model.TeamDetails;
import com.footballapp.utils.Constants;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.footballapp.utils.Constants.VERSES;
import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;
import static org.junit.Assert.assertEquals;

public class SortScheduleImplTest {

    SortScheduleImpl sortScheduleImpl;

    private List<String> matchListInput = new ArrayList<>();

    @Before
    public void setUp() {
        sortScheduleImpl = new SortScheduleImpl();

        MockitoAnnotations.initMocks(this);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                matchListInput.add("Team ".concat(String.valueOf(i)).concat(Constants.VERSES.concat("Team ".concat(String.valueOf(j)))));
            }
        }
    }

    @Test
    public void handleUnsortedHomeAwayMatchesTest() {


        assertEquals(matchListInput.get(0), "Team 0_vs_Team 0");
        assertEquals(matchListInput.get(4), "Team 1_vs_Team 1");
        assertEquals(matchListInput.get(matchListInput.size() - 1), "Team 2_vs_Team 2");
        assertEquals(matchListInput.size(), 9);

        List matchSortedList = sortScheduleImpl.handleUnsortedHomeAwayMatches(new ArrayList(), matchListInput);

        assertEquals(matchSortedList.get(0), "Team 0_vs_Team 0");
        assertEquals(matchSortedList.get(4), "Team 1_vs_Team 2");
        assertEquals(matchListInput.size(), 0);
        assertEquals(matchSortedList.size(), 9);
    }

    @Test
    public void shouldHandleUnSchedulableMatchesTest() {

        matchListInput.add("Team 1_vs_Team 1");
        matchListInput.add("Team 1_vs_Team 1");
        matchListInput.add("Team 1_vs_Team 1");
        matchListInput.add("Team 1_vs_Team 1");
        matchListInput.add("Team 1_vs_Team 1");
        assertEquals(matchListInput.size(), 14);

        assertEquals(matchListInput.get(0), "Team 0_vs_Team 0");
        assertEquals(matchListInput.get(4), "Team 1_vs_Team 1");
        assertEquals(matchListInput.get(matchListInput.size() - 1), "Team 1_vs_Team 1");

        List matchSortedList = sortScheduleImpl.handleUnsortedHomeAwayMatches(new ArrayList(), matchListInput);

        assertEquals(matchSortedList.get(0), "Team 0_vs_Team 0");
        assertEquals(matchSortedList.get(12), "Team 1_vs_Team 1");
        assertEquals(matchSortedList.get(11), "Team 1_vs_Team 1");
        assertEquals(matchListInput.size(), 0);
        assertEquals(matchSortedList.size(), 14);
    }

}


