package com.example.poll.simple_poll2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/polls")
public class PollController {

    private final PollService pollService;

    @Autowired
    public PollController(PollService pollService) {
        this.pollService = pollService;
    }

    @GetMapping("/options")
    public List<String> getOptions() {
        return pollService.getAvailableOptions();
    }

    @PostMapping("/vote")
    public ResponseEntity<String> vote(@RequestBody VoteRequest request) {
        String option = request.getOption();

        if (option == null || option.isEmpty() || !pollService.getAvailableOptions().contains(option)) {
            return new ResponseEntity<>("투표할 항목을 다시 확인해 주세요.", HttpStatus.BAD_REQUEST);
        }

        pollService.vote(option);
        return new ResponseEntity<>("투표가 성공적으로 처리되었습니다: " + option, HttpStatus.OK);
    }

    @GetMapping("/results")
    public Map<String, Object> getResults() {
        return Map.of(
                "success", true,
                "totalVotes", pollService.getTotalVotes(),
                "results", pollService.getResults()
        );
    }

    @PostMapping("/reset")
    public String resetPoll() {
        pollService.resetVotes();
        return "투표 기록이 초기화되었습니다.";
    }
}
