package com.originalalex.github.ranking;

import com.originalalex.github.functionalities.Function;
import com.originalalex.github.helper.UserID;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.Color;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SimpleDisplay implements Function {

    private String secondArgument;
    private UserID user;
    private DatabaseCommunicator db = DatabaseCommunicator.getInstance();
    private int rankupLevel;

    public SimpleDisplay(int rankupEveryXLevels) {
        this.rankupLevel = rankupEveryXLevels;
    }

    @Override
    public void handle(MessageReceivedEvent e, String[] parts) {
        this.secondArgument = parts[1];
        switch(secondArgument) {
            case "show":
            case "reveal":
                showRating(e.getAuthor().getName(), e.getGuild().getId(), e.getAuthor().getId(), e);
                break;
            case "rep":
            case "help":
                showHelpMessage(e);
            default: // in case it is a user
                List<User> taggedUsers = e.getMessage().getMentionedUsers();
                if (taggedUsers.size() == 1) { // second argument is a user and they want the stats of a certain user
                    User user = taggedUsers.get(0);
                    showRating(user.getName(), e.getGuild().getId(), user.getId(), e);
                }
        }
    }

    public SimpleDisplay() {
        this.user = new UserID();
    }

    private void showHelpMessage(MessageReceivedEvent e) {
        MessageEmbed embed = new EmbedBuilder()
                .setColor(Color.CYAN)
                .setTitle("Rating Help")
                .setDescription("The rating system is to evaluate a user's value to a server. The following are valid commands for ratings:\n" +
                "__**neptune.rep [+/-] [User]**__ = Increases or decreases the reputation of a user, and states the user's new rating\n" +
                "__**neptune.rep show and neptune.show**__ = Show information about your reputation including your rating, the number of people who rated you positively, and the number of people who rated you negatively\n" +
                "__**neptune.rep [NAME]**__ = Shows similar information about a specified player\n" +
                "__**neptune.rep leaderboards [size]**__ = Show the top n players with the highest rating, or the top ten if size is left unspecified\n" +
                "__**neptune.rep**__ = Display this information")
                .build();
        e.getChannel().sendMessage(embed).queue();
    }

    private void showRating(String name, String guildID, String ID, MessageReceivedEvent e) {
        ResultSet info = db.fetchRow(guildID, ID);
        try {
            if (info.next()) { // There is an entry under their name
                int rating = info.getInt("rating");
                int posRatings = info.getInt("posRatings");
                int negRatings = info.getInt("negRatings");
                MessageEmbed embed = new EmbedBuilder().setColor(Color.CYAN)
                        .setTitle("Rating")
                        .setDescription("Information about " + name + "'s  rating:\n" +
                        "[✓] __Level__: " + rating / rankupLevel + "\n" +
                        "[✓] __Rating__: " + rating + "\n" +
                        "[✓] __Positive Ratings Received__: " + posRatings + "\n" +
                        "[✓] __Negative Ratings Received__: " + negRatings)
                        .build();
                e.getChannel().sendMessage(embed).queue();
            } else {
                MessageEmbed embed = new EmbedBuilder().setColor(Color.CYAN)
                        .setTitle("Rating")
                        .setDescription("You do not have an entry in the rating database, but the default rating is 0")
                        .build();
                e.getChannel().sendMessage(embed).queue();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
