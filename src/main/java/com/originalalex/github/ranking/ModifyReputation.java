package com.originalalex.github.ranking;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.originalalex.github.functionalities.Function;
import com.originalalex.github.helper.FileHelper;
import com.originalalex.github.helper.NumberParser;
import com.originalalex.github.helper.ReadableTime;
import com.originalalex.github.helper.UserID;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.MessageHistory;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.Color;
import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ModifyReputation implements Function {

    private String[] parts;
    private UserID helperClass;
    private DatabaseCommunicator db;
    private RankingCalculator rankingCalculator;

    private Map<Integer, String> emotesAndLevels;
    private List<String> usersWhoCanSetrep;
    private List<String> usersWhoCanWipeDB;
    private int getLevel;

    public ModifyReputation() {
        this.helperClass = new UserID();
        this.rankingCalculator = new RankingCalculator();
        this.db = DatabaseCommunicator.getInstance();
        initializeEmotesMapAndUsers();
    }

    @Override
    public void handle(MessageReceivedEvent e, String[] parts) {
        boolean positive;
        switch (parts[1]) {
            case "+":
            case"plus":
                positive = true;
                break;
            case "-":
            case "minus":
                positive = false;
                break;
            default:
                invalidTarget(e, parts[2]);
                return;
        }
        String senderUserID = e.getMessage().getAuthor().getId();
        String targetUserID = parts[2];

        if (senderUserID.equals(targetUserID)) {
            noSendingToYourself(e);
            return;
        }

        int cooldown = db.getCooldown(senderUserID, targetUserID);
        if (cooldown != -1) { // there is still an active cooldown
            MessageEmbed embed = new EmbedBuilder()
                    .setColor(Color.CYAN)
                    .setTitle("Rating Cooldown")
                    .setDescription("You still have an active cooldown of " + ReadableTime.convertSeconds(cooldown) + " remaining")
                    .build();
            e.getChannel().sendMessage(embed).queue();
            return;
        }

        String guild = e.getGuild().getId();
        int sendersRating = getRating(e, senderUserID);

        if (helperClass.isValidUserID(e, targetUserID)) {
            ResultSet infoOnTarget = db.fetchRow(guild, targetUserID);
            try {
                int targetsRating;
                int targetsPositiveRatings;
                int targetsNegativeRatings;
                if (!infoOnTarget.next()) {
                    db.setData(guild, targetUserID, 0, 0, 0);
                    targetsRating = 0; // Default values:
                    targetsPositiveRatings = 0;
                    targetsNegativeRatings = 0;
                } else {
                    targetsRating = infoOnTarget.getInt("rating");
                    targetsPositiveRatings = infoOnTarget.getInt("posRatings");
                    targetsNegativeRatings = infoOnTarget.getInt("negRatings");
                }

                if (positive) { // If the rating was positive
                    targetsPositiveRatings++;
                } else {
                    targetsNegativeRatings++;
                }
                int targetsNewRating = rankingCalculator.calculateNewRating(targetsRating, positive); // calculate new rating
                db.setData(guild, targetUserID, targetsNewRating, targetsPositiveRatings, targetsNegativeRatings); // Update the entry with the new rating/pos/neg rating values

                if (hasRankedUp(positive, targetsNewRating)) { // send a rankup message
                    e.getChannel().sendMessage("Congratulations, you have levelled up to level " + targetsNewRating / getLevel + "!");
                }

                String emoji = getEmojiToUse(positive, targetsNewRating);
                MessageHistory history = e.getChannel().getHistory();
                history
                        .retrievePast(100)
                        .queue(messages -> messages.stream()
                                            .filter(message -> message.getAuthor().getId().equals(targetUserID))
                                            .limit(1)
                                            .forEach(m -> m.addReaction(emoji).queue()));

                db.addCooldown(senderUserID, targetUserID);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private boolean hasRankedUp(boolean positive, int level) {
        if (!positive) {
            return false;
        }
        return level % getLevel == 0;
    }

    private String getEmojiToUse(boolean positiveRating, int level) {
        if (!positiveRating) { // Never changes
            return "❌";
        }
        for (Map.Entry<Integer, String> entry : emotesAndLevels.entrySet()) { // loop through all entries
            if (level >= entry.getKey()) {
                return entry.getValue();
            }
        }
        return null;
    }

    private void initializeEmotesMapAndUsers() { // Read the options and add the information on emotes
        try {
            String jsonFile = FileHelper.getFileAsString(Charset.defaultCharset());
            JsonParser parser = new JsonParser();
            JsonObject root = parser.parse(jsonFile).getAsJsonObject();
            JsonArray emotesArray = root.getAsJsonArray("rep_emotes");

            emotesAndLevels = new TreeMap<>(Collections.reverseOrder()); // Keys should be in descending order
            getLevel = root.get("rep_rankup_every_x_levels").getAsInt();
            usersWhoCanWipeDB = new ArrayList<>();
            usersWhoCanSetrep = new ArrayList<>();

            for (int i = 0; i < emotesArray.size(); i++) { // Start from the end of the array (so we get the numbers in descending order
                JsonElement obj = emotesArray.get(i);
                JsonObject emoteInfo = obj.getAsJsonObject();
                String emoji = emoteInfo.get("emote").getAsString();
                int level = emoteInfo.get("emote_level").getAsInt();
                emotesAndLevels.put(level, emoji);
            }

            JsonArray ids = root.getAsJsonArray("id_of_users_who_can_setrep");

            for (JsonElement el : ids) {
                usersWhoCanSetrep.add(el.getAsString());
            }

            JsonArray ids2 = root.getAsJsonArray("id_of_users_who_can_wipedb");
            for (JsonElement el : ids) {
                usersWhoCanWipeDB.add(el.getAsString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void noSendingToYourself(MessageReceivedEvent e) { // A message that informs the user they can't rep themselves
        MessageEmbed embed = new EmbedBuilder()
                .setColor(Color.CYAN)
                .setTitle("Rating")
                .setDescription(e.getAuthor().getName() + ", you cannot rep yourself!")
                .build();
        e.getChannel().sendMessage(embed).queue();
    }

    private void invalidTarget(MessageReceivedEvent e, String target) { // A message that informs the user their target user was invalid
        MessageEmbed embed = new EmbedBuilder()
                .setColor(Color.CYAN)
                .setTitle("Rating")
                .setDescription("I was unable to find " + target + "!")
                .build();
        e.getChannel().sendMessage(embed).queue();
    }

    private int getRating(MessageReceivedEvent e, String id) { // get the rating of a user
        ResultSet infoOnSender = db.fetchRow(e.getChannel().getName(), id);
        try {
            int rating;
            if (infoOnSender.next()) { // The user has an entry in the database and we can extrapilate their rating
                rating = infoOnSender.getInt("rating");
            } else { // we must use the default value of 1000 [NOTE] - We don't have to add them to our database yet as no need to
                rating = 1000;
            }
            return rating;
        } catch (SQLException ex) {
            return -1;
        }
    }

    // Set rating [ADMIN COMMAND]

    public void setRep(MessageReceivedEvent e) { // Will be of the format [neptune.setrep @[NAME] [LEVEL]
        if (!usersWhoCanSetrep.contains(e.getAuthor().getId())) {
            e.getChannel().sendMessage("You do not have permission to perform this command!").queue();
            return;
        }
        String[] parts = e.getMessage().getStrippedContent().split(" ");
        List<User> taggedUsers = e.getMessage().getMentionedUsers();
        if (taggedUsers.size() == 1) {
            NumberParser parser = new NumberParser();
            int val = parser.parse(parts[2]);
            ResultSet resultSet = db.fetchRow(e.getGuild().getId(), taggedUsers.get(0).getId());
            int posRatings = 0, negRatings = 0;
            try {
                if (resultSet.next()) {
                    posRatings = resultSet.getInt("posRatings");
                    negRatings = resultSet.getInt("negRatings");
                } else {
                    posRatings = 0;
                    negRatings = 0;
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            if (val != Integer.MIN_VALUE) {
                db.setData(e.getGuild().getId(), taggedUsers.get(0).getId(), val, posRatings, negRatings);
                MessageEmbed embed = new EmbedBuilder()
                        .setColor(Color.CYAN)
                        .setTitle("Rating")
                        .setDescription("Set ranking of " + taggedUsers.get(0).getName() + " to " + val + ".")
                        .build();
                e.getChannel().sendMessage(embed).queue();
            } else {
                e.getChannel().sendMessage("Please input a valid level!");
            }
        }
    }

    // Getters:

    public int getLevel() {
        return this.getLevel;
    }

    public List<String> getWipeDatabaseUsers() {
        return this.usersWhoCanWipeDB;
    }

}
