package com.originalalex.github.functionalities;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class BeautifulPic implements Function {

    @Override
    public void handle(MessageReceivedEvent e, String[] parts) {
        long start = System.currentTimeMillis();
        JsonObject page = readPageAsJson("https://www.reddit.com/r/EarthPorn/.json?top/?sort=top&t=day/.json");
        if (page == null || !page.has("data")) {
            return;
        }
        JsonObject data = page.getAsJsonObject("data");
        if (!data.has("children")) {
            return;
        }
        JsonArray children = data.getAsJsonArray("children");

        JsonElement child = children.get(0);
        JsonObject childObject = child.getAsJsonObject();
        JsonObject childData = childObject.getAsJsonObject("data");
        String title = childData.getAsJsonPrimitive("title").getAsString();
        String url = childData.getAsJsonPrimitive("url").getAsString();
        try {
            InputStream inputStream = new URL(url).openStream();
            byte[] bytes = IOUtils.toByteArray(inputStream);
            Message message = new MessageBuilder().append("```fix\n" + title + "\n```").build();
            e.getChannel().sendFile(bytes, "EarthPorn.png", message).queue();
        } catch (IOException ex) {
            System.out.println("ay");
            ex.printStackTrace();
        }
    }

    private JsonObject readPageAsJson(String url) {
        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6") // User agent for connections
                    .referrer("http://www.google.com")
                    .ignoreContentType(true)
                    .get();
            return new JsonParser().parse(doc.select("body").text()).getAsJsonObject(); // the body contains all the JSON
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
