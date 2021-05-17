package com.footballapp.model;

import java.util.Date;
import java.util.List;

public class FinalSchedule {

    private List<Match> matchList;

    private Date scheduleDate;

    public List<Match> getMatchList() {
        return matchList;
    }

    public void setMatchList(List<Match> matchList) {
        this.matchList = matchList;
    }

    public Date getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(Date scheduleDate) {
        this.scheduleDate = scheduleDate;
    }
}
