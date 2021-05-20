package com.footballapp.scheduler;

import com.footballapp.utils.Constants;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.footballapp.utils.Constants.VERSES;
import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;

public class SortScheduleImpl implements ISortScedule {

    Map teamLastPlayedLocationStatus = new ConcurrentHashMap();

    @Override
    public synchronized List<String> handleUnsortedHomeAwayMatches(List matchScheduleSorted, List matchCombinationsStringFromInput) {

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
            if (unsortedListIterationCount > unsortedListSize +1) {
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

        LOGGER.info("Football Scheduler : Generateing unsorrted schedule from Match combinations ");

        return unsortedListIterationCount;
    }


    private void addToSortedList(List<String> matchScheduleSorted, Map teamLastPlayedLocationStatus, Iterator iter, String teamHome, String teamAway) {
        matchScheduleSorted.add(teamAway.concat(VERSES).concat(teamHome));
        iter.remove();

        teamLastPlayedLocationStatus.put(teamHome, Constants.MatchLocation.AWAY);
        teamLastPlayedLocationStatus.put(teamAway, Constants.MatchLocation.HOME);
    }
}
