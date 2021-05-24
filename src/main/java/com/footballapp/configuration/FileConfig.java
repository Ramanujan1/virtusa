package com.footballapp.configuration;

import com.footballapp.model.TeamDetails;
import com.footballapp.scheduler.ISortScedule;
import com.footballapp.scheduler.SortScheduleImpl;
import com.footballapp.utils.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.List;

@Configuration
public class FileConfig {

    @Bean(name = "teamDetailsList")
    public List<TeamDetails> getTeamDetails() throws IOException {

        final ILoadTeamsData loadTeamsData = LoadDataFactory.getFileLoadClass(Constants.FileLoadMode.LOAD_ON_STARTUP);

        return loadTeamsData.getTeamDetails();
    }

    @Bean
    public ISortScedule getSortSchedule() throws IOException {

        return new SortScheduleImpl();
    }
}
