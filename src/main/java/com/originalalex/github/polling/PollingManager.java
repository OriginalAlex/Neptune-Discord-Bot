package com.originalalex.github.polling;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.originalalex.github.helper.FileHelper;
import com.originalalex.github.helper.NumberParser;
import com.originalalex.github.helper.ReadableTime;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.List;

public class PollingManager {

    private PollingParser parser;
    private NumberParser numberParser;
    private int maxPollDuration;
    private List<String> usersWhoCanDeletePolls;

    private Map<Integer, Poll> pollMap;

    private Random random; // Used for generating random IDs

    public PollingManager() {
        this.parser = new PollingParser();
        this.numberParser = new NumberParser();
        this.random = new Random();
        this.pollMap = new HashMap<>();

        String config = FileHelper.getFileAsString(Charset.defaultCharset());
        JsonParser parser = new JsonParser();
        JsonObject root = parser.parse(config).getAsJsonObject();
        this.maxPollDuration = root.get("max_poll_duration_in_minutes").getAsInt();
        this.usersWhoCanDeletePolls = new ArrayList<>();

        JsonArray users = root.getAsJsonArray("can_delete_polls");
        for (JsonElement e : users) {
            usersWhoCanDeletePolls.add(e.getAsString());
        }
    }

    private void handle(MessageReceivedEvent e, String[] parts, double duration) {
        String message = e.getMessage().getStrippedContent();
        List<String> arguments = parser.parse(message);
        if (arguments.size() <= 2 || arguments.size() >= 6) { // invalid message
            e.getChannel().sendMessage("Please create a valid poll. eg. __neptune.poll (Dogs or cats?) [Dogs] [Cats]").queue();
            return;
        }
        String question = arguments.get(0);
        int randID = random.nextInt(900) + 100; // make a random 3 digit ID
        while (pollMap.containsKey(randID)) {
            randID = random.nextInt(900) + 100; // repeat til unique
        }

        String successMessage = "The poll was successfully created: " + question + ". Vote with __neptune.vote " + randID + " [#]__, the following are options:\n";

        List<String> values = new ArrayList<>();

        for (int i = 1; i < arguments.size(); i++) {
            String argument = arguments.get(i);
            successMessage += "(" + i + ") " + argument;
            if (i != arguments.size() -1) {
                successMessage += "\n";
            }
            values.add(argument); // add default values
        }

        Poll poll = new Poll(randID, question, e.getMessage().getAuthor().getId(), values, this);
        poll.startTimer(e, duration);
        pollMap.put(randID, poll);

        e.getChannel().sendMessage(successMessage).queue();
    }

    public void vote(MessageReceivedEvent e) { // will be of format: neptune.vote [ID] [NUMBER]
        String message = e.getMessage().getStrippedContent();
        String[] parts = message.split(" ");
        String id = parts[1];

        int idValue = numberParser.parse(id);

        if (!pollMap.containsKey(idValue)) {
            e.getChannel().sendMessage("Please enter a valid poll ID").queue();
            return;
        }

        String voteString = parts[2];
        int vote = numberParser.parse(voteString);

        Poll poll = pollMap.get(idValue);

        if (vote < 1 || vote > poll.getNumberOfOptions()) {
            e.getChannel().sendMessage("Please enter a valid option").queue();
            return;
        }

        poll.vote(e, vote);

    }

    private void displayInformation(MessageReceivedEvent e) { // will be of format: neptune.poll show [ID]
        String[] parts = e.getMessage().getStrippedContent().split(" ");
        String number = parts[2];
        int id = numberParser.parse(number);
        if (!pollMap.containsKey(id)) {
            e.getChannel().sendMessage("Please enter a valid poll ID").queue();
            return;
        }
        Poll poll = pollMap.get(id);
        poll.displayInformation(e);
    }

    public void handleAllCases(MessageReceivedEvent e, String[] parts) {
        if (parts.length > 3) {
            String timeDuration = parts[1];
            double val = numberParser.parseDouble(timeDuration);

            if (val <= 0) {
                e.getChannel().sendMessage("Please enter a valid time duration!").queue();
                return;
            } else if (val > maxPollDuration) {
                e.getChannel().sendMessage("The maximum duration for a poll is " + maxPollDuration + ". Your poll's duration cannot exceed this").queue();
                return;
            }
            handle(e, parts, val);
        }
        else if (parts[1].equalsIgnoreCase("show") || parts[1].equalsIgnoreCase("info")) {
            if (parts.length == 3) { // they have specified a poll
                displayInformation(e);
            } else if (parts.length == 2) { // they request information on ever active poll
                showInfoOnAllPolls(e);
            }
        } else if ((parts[1].equals("stop") || parts[1].equalsIgnoreCase("end")) && parts.length == 3) {
            String possibleID = parts[2];
            int number = numberParser.parse(possibleID);
            if (!pollMap.containsKey(number)) {
                e.getChannel().sendMessage("You must enter a valid poll ID");
                return;
            }
            Poll poll = pollMap.get(number);
            poll.endPrematurely(e);
        } else if (parts[1].equalsIgnoreCase("delete") && parts.length == 3) {
            String possibleID = parts[2];
            int number = numberParser.parse(possibleID);
            if (!pollMap.containsKey(number)) {
                e.getChannel().sendMessage("You must enter a valid poll ID");
                return;
            }
            deletePoll(e, number);
        }
    }

    private void deletePoll(MessageReceivedEvent e, int id) {
        String authorID = e.getAuthor().getId();
        if (usersWhoCanDeletePolls.contains(authorID)) {
            if (pollMap.containsKey(id)) {
                pollMap.remove(id);
                e.getMessage().addReaction("\uD83D\uDC4D"); // add thumbs up to signify it was a success
            } else {
                e.getChannel().sendMessage("There is no active poll with the ID " + id).queue();
            }
        } else {
            e.getAuthor().openPrivateChannel().queue(message -> {
                message.sendMessage("You do not have valid permission to perform that command!");
            });
            e.getMessage().addReaction("❌").queue();
        }
    }

    private void showInfoOnAllPolls(MessageReceivedEvent e) {
        List<Poll> polls = new ArrayList<>();
        String info = "The following are the active polls:\n";
        for (Map.Entry<Integer, Poll> entry : pollMap.entrySet()) {
            Poll poll = entry.getValue();
            info += "(" + entry.getKey() + ") **" + poll.getQuestion() + "**: *" + ReadableTime.convertSeconds(poll.getTimeRemaining()) + "*\n";
        }
        info = info.substring(0, info.lastIndexOf("\n"));
        MessageEmbed information = new EmbedBuilder()
                .setColor(Color.CYAN)
                .setTitle("Poll Information")
                .setDescription(info)
                .build();
        e.getChannel().sendMessage(information).queue();
    }

    public void removePollFromID(int id) {
        pollMap.remove(id);
    }
}
