package com.footballapp.controller;

import com.footballapp.scheduler.ScheduleComputationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

@Controller
public class ScheduleController {

    @Autowired
    ScheduleComputationService scheduleComputationService;

    @GetMapping("/createSchedule")
    public String greeting( Model model) {

        return "matchSchedule";
    }

    @PostMapping("/generateSchedule")
    public String greetingSubmit(@RequestParam String  scheduleDate, Model model) throws ParseException {

        model.addAttribute("matches", scheduleComputationService.generateFinalScheduleByDate(new SimpleDateFormat("yyyy-mm-dd").parse(scheduleDate)).getMatchList());
        model.addAttribute("scheduleDate", scheduleDate);

        return "matchSchedule";
    }
}