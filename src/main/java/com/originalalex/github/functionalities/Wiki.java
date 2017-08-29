package com.originalalex.github.functionalities;

import com.originalalex.github.scrape.Scrape;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;

/**
 * Created by Alex on 28/08/2017.
 */
public class Wiki extends Function {

    private String query;
    private Scrape scrape;

    public Wiki(Scrape scrape) {
        this.query = query;
        this.scrape = scrape;
    }

    @Override
    public void handle(MessageReceivedEvent e) {
        String firstParagraph = scrape.getFirstWikiParagraph(query);
        MessageEmbed embed;
        if (firstParagraph == null) {
            embed = new EmbedBuilder()
                    .setTitle("No article regarding the topic " + query + ".")
                    .build();

        } else {
            embed = new EmbedBuilder()
                    .setColor(Color.CYAN)
                    .setTitle(query)
                    .setColor(Color.MAGENTA)
                    .setDescription(firstParagraph)
                    .build();
        }
        e.getChannel().sendMessage(embed).queue();
    }

    public void setQuery(String query) {
        this.query = query.replaceAll("\\\\", "");
    }

}
