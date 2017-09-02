package com.originalalex.github.chatterbot;

import com.google.code.chatterbotapi.*;
import com.originalalex.github.functionalities.Function;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jsoup.Jsoup;

public class Talk implements Function {

    private ChatterBotSession session;

    public Talk() {
        ChatterBotFactory factory = new ChatterBotFactory(); // create factory
        try {
            ChatterBot cleverBot = factory.create(ChatterBotType.PANDORABOTS, "cf7aa84b0e34555c");
            this.session = cleverBot.createSession();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handle(MessageReceivedEvent e) {
        String message = e.getMessage().getStrippedContent();
        message = message.split(" ", 2)[1]; // remove the neptune.cleverbot part
        try {
            String response = session.think(message);
            e.getChannel().sendMessage(Jsoup.parse(response).text()).queue(); // Remove the HTML tags from the string
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
