package com.originalalex.github.ranking;

import com.originalalex.github.functionalities.Function;
import com.originalalex.github.helper.UserID;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ModifyReputation implements Function {

    private String[] parts;
    private UserID helperClass;
    private DatabaseCommunicator db;
    private RankingCalculator rankingCalculator;

    public ModifyReputation() {
        this.helperClass = new UserID();
        this.rankingCalculator = new RankingCalculator();
        this.db = DatabaseCommunicator.getInstance();
    }

    @Override
    public void handle(MessageReceivedEvent e) {
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
            default: System.out.println("Not valid input " + parts[1]); return;
        }
        String senderUserID = e.getMessage().getAuthor().getId();
        String targetUserID = parts[2];
        String channel = e.getGuild().getName();
        int sendersRating = getRating(e, senderUserID);

        if (helperClass.isValidUserID(e, targetUserID)) {
            ResultSet infoOnTarget = db.fetchRow(channel, targetUserID);
            try {
                int targetsRating;
                int targetsPositiveRatings;
                int targetsNegativeRatings;
                if (!infoOnTarget.next()) {
                    db.setData(channel, targetUserID, 1000, 0, 0);
                    targetsRating = 1000; // Default values:
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
                int targetsNewRating = rankingCalculator.calculateNewRating(sendersRating, targetsRating, targetsPositiveRatings, targetsNegativeRatings, positive); // calculate new rating
                db.setData(channel, targetUserID, targetsNewRating, targetsPositiveRatings, targetsNegativeRatings); // Update the entry with the new rating/pos/neg rating values

                MessageEmbed embed = new EmbedBuilder().setColor(Color.CYAN)
                        .setTitle("Rating")
                        .setDescription("Due to " + e.getAuthor().getName() + "'s rating, your new rating is " + targetsNewRating + ", " + helperClass.getMember(e, targetUserID).getEffectiveName() + ".")
                        .build();
                e.getChannel().sendMessage(embed).queue();

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

    }

    private int getRating(MessageReceivedEvent e, String id) {
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

    public void setParts(String[] t) {
        this.parts = t;
    }

}
