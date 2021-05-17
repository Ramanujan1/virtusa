package com.footballapp.configuration;

import com.footballapp.model.TeamDetails;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Configuration
public class FileConfig {

    @Bean(name = "teamDetailsList")
    public List<TeamDetails> getTeamDetails() throws IOException {

        ILoadTeamsData loadTeamsData = new FileDataload();

        return loadTeamsData.getTeamDetails();
    }
}
