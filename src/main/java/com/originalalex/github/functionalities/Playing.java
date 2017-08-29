package com.originalalex.github.functionalities;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Playing implements Function {

    private JDA jda;
    private String game;

    public Playing(JDA jda) {
        this.jda = jda;
    }

    @Override
    public void handle(MessageReceivedEvent e) {
        jda.getPresence().setGame(Game.of(game));
    }

    public void setGame(String game) {
        this.game = game;
    }
}
