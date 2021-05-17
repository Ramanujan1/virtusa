package com.footballapp.scheduler;

import com.footballapp.utils.Constants;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.footballapp.utils.Constants.Verces;

public class SortScheduleImpl implements ISortScedule {

    Map teamLastPlayedLocationStatus = new ConcurrentHashMap();

    @Override
    public List<String> handleUnsortedHomeAwayMatches(List matchScheduleSorted, List matchCombinationsStringFromInput) {

        int unsortedListIterationCount = 0;
        while (matchCombinationsStringFromInput.size() > 0) {
            Iterator unsortedListIterator = matchCombinationsStringFromInput.iterator();

            while (unsortedListIterator.hasNext()) {

                unsortedListIterationCount++;

                if (unsortedListIterationCount > 2) {
                    // handle special case where the top most match combination in the unsorted list cannot be scheduled
                    // in such cases swap the top most element with the last element and continue the iteration
                    String lastMatch = (String) matchCombinationsStringFromInput.get(matchCombinationsStringFromInput.size() - 1);
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

    private void initializeStausMap(Map teamLastPlayedLocationStatus, String[] teamArray) {
        if (teamLastPlayedLocationStatus.get(teamArray[0]) == null) {
            teamLastPlayedLocationStatus.put(teamArray[0], "");
        }

        if (teamLastPlayedLocationStatus.get(teamArray[1]) == null) {
            teamLastPlayedLocationStatus.put(teamArray[1], "");
        }
    }


    private int getUnsortedListIterationCount(List<String> matchScheduleSorted, Map teamLastPlayedLocationStatus, int unsortedListIterationCount, Iterator unsortedListIterator, String[] teamArray) {
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


    private void addToSortedList(List<String> matchScheduleSorted, Map teamLastPlayedLocationStatus, Iterator iter, String teamHome, String teamAway) {
        matchScheduleSorted.add(teamAway.concat(Constants.Verces).concat(teamHome));
        iter.remove();

        teamLastPlayedLocationStatus.put(teamHome, Constants.MatchLocation.AWAY);
        teamLastPlayedLocationStatus.put(teamAway, Constants.MatchLocation.HOME);
    }
}
