package com.originalalex.github.functionalities;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageHistory;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.List;

/**
 * Created by Alex on 29/08/2017.
 */
public class Purge extends Function {

    private int depth;

    public void handle(MessageReceivedEvent e) {
        User user = e.getAuthor();
        MessageHistory history = e.getChannel().getHistory();
        List<Message> retrievedHistory = history.getRetrievedHistory();
        int depthSoFar = 0;
        for (Message m : retrievedHistory) {
            if (depthSoFar == depth) { // If we have gone far enough into the history
                break;
            }
            if (m.getAuthor().getName().equals("PQE")) {
                m.delete();
                depthSoFar++;
            }
        }
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

}
