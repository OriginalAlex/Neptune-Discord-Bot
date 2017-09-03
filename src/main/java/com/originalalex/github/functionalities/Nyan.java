package com.originalalex.github.functionalities;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Nyan implements Function {

    @Override
    public void handle(MessageReceivedEvent e, String[] parts) {
        MessageEmbed embed = new EmbedBuilder()
                .setDescription("░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░\n" +
                        "░░░░░░░░░░▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄░░░░░░░░░\n" +
                        "░░░░░░░░▄▀░░░░░░░░░░░░▄░░░░░░░▀▄░░░░░░░\n" +
                        "░░░░░░░░█░░▄░░░░▄░░░░░░░░░░░░░░█░░░░░░░\n" +
                        "░░░░░░░░█░░░░░░░░░░░░▄█▄▄░░▄░░░█░▄▄▄░░░\n" +
                        "░▄▄▄▄▄░░█░░░░░░▀░░░░▀█░░▀▄░░░░░█▀▀░██░░\n" +
                        "░██▄▀██▄█░░░▄░░░░░░░██░░░░▀▀▀▀▀░░░░██░░\n" +
                        "░░▀██▄▀██░░░░░░░░▀░██▀░░░░░░░░░░░░░▀██░\n" +
                        "░░░░▀████░▀░░░░▄░░░██░░░▄█░░░░▄░▄█░░██░\n" +
                        "░░░░░░░▀█░░░░▄░░░░░██░░░░▄░░░▄░░▄░░░██░\n" +
                        "░░░░░░░▄█▄░░░░░░░░░░░▀▄░░▀▀▀▀▀▀▀▀░░▄▀░░\n" +
                        "░░░░░░█▀▀█████████▀▀▀▀████████████▀░░░░\n" +
                        "░░░░░░████▀░░███▀░░░░░░▀███░░▀██▀░░░░░░\n" +
                        "░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░")
                .build();
        e.getChannel().sendMessage(embed).queue();
    }
}
