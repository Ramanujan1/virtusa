package com.footballapp.controller;

import com.footballapp.scheduler.ScheduleComputationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;

@Controller
public class ScheduleController {

    @Autowired
    ScheduleComputationService scheduleComputationService;

    @GetMapping("/createSchedule")
    public String greeting(Model model) {

        LOGGER.info("Football Scheduler : Render Home page ");
        return "matchSchedule";
    }

    @PostMapping("/generateSchedule")
    public String greetingSubmit(@RequestParam String scheduleDate, Model model) throws ParseException {

        if (scheduleDate == null || scheduleDate.trim().equals("")) {
            model.addAttribute("error", "Enter a valid date");
        } else {
            Date scheduleDateObject = new SimpleDateFormat("yyyy-mm-dd").parse(scheduleDate);
            SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
            String scheduleDateFinal = formatter.format(scheduleDateObject);

            model.addAttribute("matches", scheduleComputationService.generateFinalScheduleByDate(scheduleDateObject).getMatchList());
            model.addAttribute("scheduleDateFinal", scheduleDateFinal);
        }

        LOGGER.info("Football Scheduler : Final Schedule constructed ");

        return "matchSchedule";
    }
}