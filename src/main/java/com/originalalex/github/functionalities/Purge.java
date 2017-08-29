package com.originalalex.github.functionalities;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageHistory;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.List;

public class Purge implements Function {

    private int depth;

    @Override
    public void handle(MessageReceivedEvent e) {
        User user = e.getAuthor();
        MessageHistory history = new MessageHistory(e.getTextChannel());
        List<Message> retrievedHistory = history.getRetrievedHistory();
        int depthSoFar = 0;
        System.out.println(retrievedHistory.size());
        retrievedHistory.stream()
                .filter(f -> f.getAuthor().equals("PQE"))
                .limit(depth)
                .forEach(Message::delete);
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

}
