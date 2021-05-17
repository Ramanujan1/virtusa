package com.footballapp.model;

import com.footballapp.model.TeamDetails;
import com.footballapp.scheduler.ScheduleComputationService;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ScheduleTest {

    @Test
    public void givenSetAndSelectionSize_whenCalculatedUsingSetRecursiveAlgorithm_thenExpectedCount() {
        ScheduleComputationService generator = new ScheduleComputationService();
        List<TeamDetails> teamDetailsList = new ArrayList<>();

        for ( int i=0 ; i <= 10; i++ ) {
            TeamDetails teamDetails = new TeamDetails();
            teamDetails.setTeamName("Team "+i);
            teamDetailsList.add(teamDetails);
        }

        long start = System.currentTimeMillis();
// some time passes

//        List<Match> selection = generator.generate(teamDetailsList);

        long end = System.currentTimeMillis();
        long elapsedTime = end - start;

        System.out.println(elapsedTime);

        long startParellel = System.currentTimeMillis();



// some time passes
//        System.out.println(startParellel);
//
//        List<Match> selectionParellel = generator.generateApproach2(teamDetailsList);
//
//        long endParellel = System.currentTimeMillis();
//        System.out.println(endParellel);
//         long elapsedTimeParellel = endParellel - startParellel;

//        System.out.println(elapsedTimeParellel);
//        System.out.println(selectionParellel.stream().map(match -> match.getTeam1().getTeamName().concat(" vs ")
//                .concat(match.getTeam2().getTeamName())
//                .concat(" Home team : ").concat(match.getHomeTeam().getTeamName())).collect(Collectors.joining("\n")));
//        System.out.println(selection);
//        assertEquals(4, selectionParellel.size());
    }
}
