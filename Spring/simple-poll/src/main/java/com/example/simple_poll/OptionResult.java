package com.example.simple_poll;

public class OptionResult {
    private final String optionName;
    private int votes;
    private double percentage;
    private boolean isLeading;

    public OptionResult(String optionName) {
        this.optionName = optionName;
    }

    public String getOptionName() {return optionName;}
    public int getVotes() {return votes;}
    public void setVotes(int votes) {this.votes = votes;}

    public double getPercentage() { return Math.round(percentage * 10.0) / 10.0; }
    public void setPercentage(double percentage) { this.percentage = percentage; }

    public boolean isLeading() {return isLeading;}
    public void setLeading(boolean leading) {this.isLeading = leading;}
}
