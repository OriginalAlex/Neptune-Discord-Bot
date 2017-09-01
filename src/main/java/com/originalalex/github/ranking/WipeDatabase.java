package com.originalalex.github.ranking;

import com.originalalex.github.functionalities.Function;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;

public class WipeDatabase implements Function {

    private DatabaseCommunicator db = DatabaseCommunicator.getInstance();

    @Override
    public void handle(MessageReceivedEvent e) {
        db.wipeDatabase();
        MessageEmbed embed = new EmbedBuilder()
                .setColor(Color.CYAN)
                .setTitle("Rating Database")
                .setDescription("Successfully cleared rating database!")
                .build();
        e.getChannel().sendMessage(embed).queue();
    }
}
