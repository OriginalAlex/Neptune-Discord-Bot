package com.originalalex.github.functionalities;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * Created by Alex on 29/08/2017.
 */
public class HorizontalLine extends Function {

    @Override
    public void handle(MessageReceivedEvent e) {
        e.getChannel().sendMessage("=======================================").queue();
    }
}
