package com.originalalex.github.functionalities;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.awt.*;
import java.io.IOException;

public class Crypto implements Function {

    private String ticker;

    private static final String COINMARKETCAP_BASE_URL = "https://coinmarketcap.com/currencies/";

    @Override
    public void handle(MessageReceivedEvent e) {
        try {
            Document doc = Jsoup.connect(COINMARKETCAP_BASE_URL + ticker)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6") // User agent for connections
                    .referrer("http://www.google.com")
                    .get();
            Element price = doc.select("span#quote_price").first();
            Elements information = doc.select("div.coin-summary-item-detail");

            Element supplyInfo = information.first();
            String usdMarketCap = supplyInfo.ownText();

            String volume = information.get(1).ownText();

            String circulatingSupply = information.get(2).ownText();

            MessageEmbed embed = new EmbedBuilder()
                    .setColor(Color.CYAN)
                    .setTitle("__" + Character.toUpperCase(ticker.charAt(0)) + ticker.substring(1) + "__") // capitalize the first letter (for aesthetic reasons)
                    .setDescription("**The following information was gathered on the currency " + ticker + ":** \n" +
                    "[✓] __Price__: " + price.text() + "\n" +
                    "[✓] __Market Cap__: " + usdMarketCap + "\n" +
                    "[✓] __24 hour Volume__: " + volume + "\n" +
                    "[✓] __Circulating Supply__: " + circulatingSupply)
                    .build();
            e.getChannel().sendMessage(embed).queue();
        } catch (IOException ex) {
            MessageEmbed errorEmbed = new EmbedBuilder()
                    .setColor(Color.CYAN)
                    .setTitle("__**No information found on the cryptocurrency " + ticker + ".**__")
                    .build();
            e.getChannel().sendMessage(errorEmbed).queue();
        }
    }

    public void setTicker(String ticker) {
        switch (ticker.toLowerCase()) {
            case "btc": this.ticker = "Bitcoin"; break; // common abbreviations
            case "eth": this.ticker = "Ethereum"; break;
            case "xrp": this.ticker = "Ripple"; break;
            default: this.ticker = ticker;
        }
    }
}
