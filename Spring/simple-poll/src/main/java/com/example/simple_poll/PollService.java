package com.example.simple_poll;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@Service
public class PollService {
    private final ConcurrentMap<String, Integer> votesMap = new ConcurrentHashMap<>();
    private final List<String> availableOptions = List.of("Java", "Python", "JavaScript", "C++");

    @PostConstruct
    public void init() {
        availableOptions.forEach(option -> votesMap.put(option, 0));
    }

    public List<String> getAvailableOptions() {
        return availableOptions;
    }

    public void vote(String optionName) {
        votesMap.computeIfPresent(optionName, (key, count) -> count + 1);
    }

    public void resetVotes() {
        availableOptions.forEach(option -> votesMap.put(option, 0));
    }

    public int getTotalVotes() {
        return votesMap.values().stream()
                .mapToInt(Integer::intValue)
                .sum();
    }
    public List<PollResult> getResults() {
        int totalVotes = getTotalVotes();

        List<PollResult> results = availableOptions.stream()
                .map(optionName -> {
                    int votes = votesMap.getOrDefault(optionName, 0);
                    double percentage = totalVotes > 0 ? ((double) votes / totalVotes) * 100 : 0.0;
                    return new PollResult(optionName, votes, percentage, false);
                })
                .collect(Collectors.toList());

        if (totalVotes > 0) {
            results.sort(Comparator.comparingInt(PollResult::getVotes).reversed());
            int maxVotes = results.get(0).getVotes();

            return results.stream()
                    .map(result -> new PollResult(
                            result.getName(),
                            result.getVotes(),
                            result.getPercentage(),
                            result.getVotes() == maxVotes && result.getVotes() > 0
                    ))
                    .collect(Collectors.toList());
        }
        return results;
    }

    public List<OptionResult> getResultsForMvc() {
        return this.getResults().stream().map(pr -> {
            OptionResult or = new OptionResult(pr.getName());
            or.setVotes(pr.getVotes());
            or.setPercentage(pr.getPercentage());
            or.setLeading(pr.isTop());
            return or;
        }).collect(Collectors.toList());
    }
}
