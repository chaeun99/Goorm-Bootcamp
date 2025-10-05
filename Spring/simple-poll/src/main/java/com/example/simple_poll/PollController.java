package com.example.simple_poll;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

// http://localhost:8080/
@Controller
public class PollController {
    private final PollService pollService;

    @Autowired
    public PollController(PollService pollService) {
        this.pollService = pollService;
    }

    @GetMapping("/")
    public String showPoll(Model model) {
        model.addAttribute("results", pollService.getResultsForMvc());
        model.addAttribute("options", pollService.getResultsForMvc());
        model.addAttribute("totalVotes", pollService.getTotalVotes());
        return "poll";
    }
    @PostMapping("/vote")
    public String handleVote(@RequestParam String option) {
        if (option != null && !option.isEmpty()) {
            pollService.vote(option);
        }
        return "redirect:/";
    }
}
