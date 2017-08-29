package com.originalalex.github.listener;

import com.originalalex.github.functionalities.HorizontalLine;
import com.originalalex.github.functionalities.Nyan;
import com.originalalex.github.functionalities.Purge;
import com.originalalex.github.functionalities.Wiki;
import com.originalalex.github.helper.NumberParser;
import com.originalalex.github.scrape.Scrape;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;

public class MessageListener extends ListenerAdapter {

    private JDA jda;
    private Scrape scrape;
    private NumberParser parser;

    // Functions:
    private Wiki wikiFunction;
    private Nyan nyan;
    private Purge purge;
    private HorizontalLine hl;

    public MessageListener(JDA jda) {
        this.jda = jda;
        this.scrape = new Scrape();
        this.parser = new NumberParser();

        this.hl = new HorizontalLine();
        this.wikiFunction = new Wiki(scrape);
        this.nyan = new Nyan();
        this.purge = new Purge();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        if (e.getAuthor().getName().equals("PQE")) {
            String[] parts = e.getMessage().getStrippedContent().split(" ");
            if (parts.length == 2) {
                switch (parts[0].toLowerCase()) {
                    case "me.wiki":
                        wikiFunction.setQuery(parts[1]);
                        wikiFunction.handle(e);
                        break;
                    case "me.purge":
                        int depth = parser.parse(parts[1]);
                        if (depth > 0) {
                            purge.setDepth(depth);
                            purge.handle(e);
                        }
                }
            } else if (parts.length == 1) {
                switch (parts[0].toLowerCase()) {
                    case "me.nyan":
                        nyan.handle(e);
                        break;
                    case "me.exit":
                    case "me.quit":
                        System.exit(0); // close the application
                        break;
                    case "me.hl":
                        hl.handle(e);
                        break;
                }
            }

        }
    }

}
