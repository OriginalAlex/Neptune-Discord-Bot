package com.originalalex.github.functionalities;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class HorizontalLine implements Function {

    @Override
    public void handle(MessageReceivedEvent e, String[] parts) {
        e.getChannel().sendMessage("==============================================").queue();
    }
}
