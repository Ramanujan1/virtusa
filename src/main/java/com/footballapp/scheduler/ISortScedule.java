package com.footballapp.scheduler;

import java.util.List;

public interface ISortScedule {

    List<String> handleUnsortedHomeAwayMatches(List matchScheduleSorted, List matchCombinationsStringFromInput);

}
