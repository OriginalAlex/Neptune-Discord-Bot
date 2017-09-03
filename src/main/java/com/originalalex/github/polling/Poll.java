package com.originalalex.github.polling;

import com.originalalex.github.helper.NumberParser;
import com.originalalex.github.helper.ReadableTime;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Poll {

    private NumberParser numberParser;
    private PollingManager manager;

    private int id;
    private String question;
    private String creatorID;
    private Map<String, Integer> optionsWithVotes;
    private Map<Integer, String> optionsID; // attach a number to each option (to make it easier to vote)
    private List<String> usersWhoHaveVoted; // list of users who have voted already
    private boolean hasEndedPrematurely = false;
    private int timeLeft;

    public Poll(int id, String question, String creatorID, List<String> options, PollingManager manager) {
        this.numberParser = new NumberParser();
        this.id = id;
        this.question = question;
        this.creatorID = creatorID;
        this.optionsWithVotes = new LinkedHashMap<>();
        this.optionsID = new HashMap<>();
        this.manager = manager;
        this.usersWhoHaveVoted = new ArrayList<>();
        for (int i = 0; i < options.size(); i++) {
            optionsID.put(i+1, options.get(i));
            optionsWithVotes.put(options.get(i), 0);
        }
    }

    public void vote(MessageReceivedEvent e, int optionID) {
        String senderID = e.getMessage().getAuthor().getId();
        if (usersWhoHaveVoted.contains(senderID)) {
            e.getChannel().sendMessage("You have already voted on this poll!").queue();
            return;
        }
        String option = optionsID.get(optionID);
        optionsWithVotes.put(option, optionsWithVotes.get(option)+1); // increment the value in the map
        e.getMessage().addReaction("\uD83D\uDC4D").queue(); // add a thumbs up to signify vote has been registered
        usersWhoHaveVoted.add(senderID);
    }

    public void startTimer(MessageReceivedEvent e, double duration) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!hasEndedPrematurely) {
                    printPollResults(e); // print out the results after the specified duration of the poll
                }
            }
        }, (long) (duration * 60 * 1000)); // convert the minutes to milliseconds for the timertask
        timeLeft = (int) duration * 60; // time left in seconds
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timeLeft--;
            }
        }, 0, 1000); // update every second
    }

    public void displayInformation(MessageReceivedEvent e) {
        String[] parts = e.getMessage().getStrippedContent().split(" ");
        String number = parts[2];
        int id = numberParser.parse(number);

        String argumentDisplay = "The following are the current options and their respective tallies:\n";
        int i = 0;

        for (Map.Entry<String, Integer> entry : optionsWithVotes.entrySet()) {
            int tally = entry.getValue();
            String option = entry.getKey();
            argumentDisplay += "(" + (++i) + ") " + option + ": " + tally;
            argumentDisplay += "\n";
        }

        argumentDisplay += "The poll has " + ReadableTime.convertSeconds(timeLeft) + " time remaining.";

        MessageEmbed embed = new EmbedBuilder()
                .setColor(Color.CYAN)
                .setTitle(question)
                .setDescription(argumentDisplay)
                .build();
        e.getChannel().sendMessage(embed).queue();
    }


    private void printPollResults(MessageReceivedEvent e) {
        Map<Integer, List<String>> optionsAsMap = new HashMap<>();
        for (Map.Entry<String, Integer> entry : optionsWithVotes.entrySet()) {
            String value = entry.getKey();
            int voters = entry.getValue();
            if (optionsAsMap.containsKey(voters)) {
                optionsAsMap.get(voters).add(value);
            } else {
                List<String> arr = new ArrayList<>();
                arr.add(value);
                optionsAsMap.put(voters,arr);
            }
        }
        SortedSet<Integer> sortedKeys = new TreeSet<>(Comparator.reverseOrder());
        optionsAsMap.keySet().stream().forEach(l -> sortedKeys.add(l));
        int top = sortedKeys.first();
        List<String> topValues = optionsAsMap.get(top);
        String announcement;
        if (topValues.size() > 1) {
            announcement = "The victors of the poll with question **" + question + "** are: *" + String.join(", ", topValues + "* with " + top + " votes.");
        } else {
            announcement = "The victor of the poll with question **" + question + "** is: " + String.join(", ", topValues + " with " + top + " votes.");
        }
        String topN = (optionsWithVotes.size() == 2) ? "two" : "three";
        announcement += "\nThe top " + topN + " are as follows:";

        int cap = 0;
        for (int i : sortedKeys) {
            if (cap++ == 3) { // only go to top 3 maximum (it would have escaped already if there were just two options)
                break;
            }
            List<String> values = optionsAsMap.get(i);
            announcement += "\n[" + String.join(", ", values) + "]: " + i;
        }
        MessageEmbed embed = new EmbedBuilder()
                .setColor(Color.CYAN)
                .setTitle(question)
                .setDescription(announcement)
                .build();
        e.getChannel().sendMessage(embed).queue();
        manager.removePollFromID(id); // remove all evidence of that id
    }

    public void endPrematurely(MessageReceivedEvent e) {
        if (e.getMessage().getAuthor().getId().equals(creatorID)) {
            this.printPollResults(e);
            hasEndedPrematurely = true;
        } else {
            e.getChannel().sendMessage("You must have created the poll to end it prematurely!").queue();
        }
    }

    public int getNumberOfOptions() {
        return optionsWithVotes.size();
    }

    public int getTimeRemaining() {
        return this.timeLeft;
    }

    public String getQuestion() {
        return this.question;
    }

}
