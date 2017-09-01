package com.originalalex.github.ranking;

import com.originalalex.github.functionalities.Function;
import com.originalalex.github.helper.UserID;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.List;

public class Leaderboards implements Function {

    private DatabaseCommunicator databaseCommunicator = DatabaseCommunicator.getInstance();
    private int depth;

    @Override
    public void handle(MessageReceivedEvent e) {
        List<Object[]> users = databaseCommunicator.getLeaderboard(depth); // Each user has an Object[] -> object[0] = their id, object[1] = their rating.

        String message = "The top " + depth + " users with the highest rating are:\n";
        int rank = 1;
        for (Object[] user : users) {
            String id = (String) user[0];
            int rating = (int) user[1];
            Member member = UserID.getMember(e, id);
            String handle = member.getEffectiveName();
            message += "(" + rank++ + ") " + handle + ": " + rating + "\n";
        }

        e.getChannel().sendMessage(message).queue();
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

}
