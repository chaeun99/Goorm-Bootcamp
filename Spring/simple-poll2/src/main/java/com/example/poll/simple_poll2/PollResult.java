package com.example.poll.simple_poll2;

public class PollResult {
    private final String name;
    private final int votes;
    private final double percentage;
    private final boolean top;

    public PollResult(String name, int votes, double percentage, boolean top) {
        this.name = name;
        this.votes = votes;
        this.percentage = percentage;
        this.top = top;
    }

    public String getName() { return name; }
    public int getVotes() { return votes; }

    public double getPercentage() { return Math.round(percentage * 10.0) / 10.0; }
    public boolean isTop() { return top; }
}
