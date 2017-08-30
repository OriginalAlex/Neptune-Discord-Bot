package com.originalalex.github.ranking;

import com.originalalex.github.functionalities.Function;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SimpleDisplay implements Function {

    private String secondArgument;
    private DatabaseCommunicator db = DatabaseCommunicator.getInstance();

    @Override
    public void handle(MessageReceivedEvent e) {
        switch(secondArgument) {
            case "show":
            case "reveal":
                showRating(e);
                break;
        }
    }

    private void showRating(MessageReceivedEvent e) {
        ResultSet info = db.fetchRow(e.getGuild().getName(), e.getAuthor().getId());
        try {
            if (info.next()) { // There is an entry under their name
                int rating = info.getInt("rating");
                int posRatings = info.getInt("posRatings");
                int negRatings = info.getInt("negRatings");
                MessageEmbed embed = new EmbedBuilder().setColor(Color.CYAN)
                        .setTitle("Rating")
                        .setDescription("Information about your rating:\n" +
                        "[✓] __Rating__: " + rating + "\n" +
                        "[✓] __Positive Ratings Received__: " + posRatings + "\n" +
                        "[✓] __Negative Ratings Received__: " + negRatings)
                        .build();
                e.getChannel().sendMessage(embed).queue();
            } else {
                MessageEmbed embed = new EmbedBuilder().setColor(Color.CYAN)
                        .setTitle("Rating")
                        .setDescription("You do not have an entry in the rating database, but the default rating is 1000")
                        .build();
                e.getChannel().sendMessage(embed).queue();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void setSecondArgument(String arg) {
        this.secondArgument = arg;
    }

}
