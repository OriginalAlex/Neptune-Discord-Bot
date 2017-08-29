package com.originalalex.github.functionalities;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * Created by Alex on 29/08/2017.
 */
public class Nyan extends Function {

    @Override
    public void handle(MessageReceivedEvent e) {
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
