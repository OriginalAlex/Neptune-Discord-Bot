package com.originalalex.github.functionalities;

import com.originalalex.github.scrape.Scrape;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;

/**
 * Created by Alex on 28/08/2017.
 */
public class Wiki implements Function {

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
                    .setColor(Color.CYAN)
                    .setTitle("__No article found regarding the topic " + query + ".__")
                    .build();

        } else if (firstParagraph.contains("perhaps you meant one of the following")) { // a list
            embed = new EmbedBuilder()
                    .setColor(Color.CYAN)
                    .setTitle("**" + query + "**")
                    .setDescription("```css\n" +
                            firstParagraph + "\n" +
                            "```")
                    .build();
        }
        else {
            embed = new EmbedBuilder()
                    .setColor(Color.CYAN)
                    .setTitle("**" + query + "**") // bolden the title
                    .setDescription(firstParagraph)
                    .build();
        }
        e.getChannel().sendMessage(embed).queue();
    }

    public void setQuery(String query) {
        this.query = query.replaceAll("\\\\", "");
    }

}
