package com.originalalex.github.ranking;

import com.originalalex.github.functionalities.Function;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.Color;
import java.util.List;

public class WipeDatabase implements Function {

    private DatabaseCommunicator db = DatabaseCommunicator.getInstance();
    private List<String> usersWithPermissions;

    public WipeDatabase(List<String> lst) {
        this.usersWithPermissions = lst;
    }

    @Override
    public void handle(MessageReceivedEvent e, String[] parts) {
        String userId = e.getAuthor().getId();
        if (!usersWithPermissions.contains(userId)) {
            e.getMessage().addReaction("❌").queue();
            return;
        }
        db.wipeDatabase();
        MessageEmbed embed = new EmbedBuilder()
                .setColor(Color.CYAN)
                .setTitle("Rating Database")
                .setDescription("Successfully cleared rating database!")
                .build();
        e.getChannel().sendMessage(embed).queue();
    }
}
