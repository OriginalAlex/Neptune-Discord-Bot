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
import java.util.Timer;
import java.util.TimerTask;

public class BeautifulPic implements Function {

    private String cachedURL;
    private String cachedTitle;

    @Override
    public void handle(MessageReceivedEvent e, String[] parts) {
        if (cachedURL == null) { // nothing is cached rn
            long start = System.currentTimeMillis();
            JsonObject page = readPageAsJson("https://www.reddit.com/r/EarthPorn/.json?top/?sort=top&t=day");
            if (page == null || !page.has("data")) {
                return;
            }
            JsonObject data = page.getAsJsonObject("data");
            if (!data.has("children")) {
                return;
            }
            JsonArray children = data.getAsJsonArray("children");
            JsonElement child = children.get(0);
            String[] parts1 = getUrlOfChild(child);
            String url = parts1[0];
            String title = parts1[1];
            Message message = new MessageBuilder().append("```fix\n" + title + "\n```\n" + url).build();
            e.getChannel().sendMessage(message).queue();
            this.cachedURL = url; // cache the image and title for 5 minutes (to reduce lag)
            this.cachedTitle = title;

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    cachedURL = null;
                    cachedTitle = null;
                }
            }, 5 * 60 * 1000);
        } else {
            Message message = new MessageBuilder().append("```fix\n" + cachedTitle + "\n```\n" + cachedURL).build();
            e.getChannel().sendMessage(message).queue();
        }
    }

    private String[] getUrlOfChild(JsonElement child) {
        JsonObject childObject = child.getAsJsonObject();
        JsonObject childData = childObject.getAsJsonObject("data");
        String title = childData.getAsJsonPrimitive("title").getAsString();
        String url = childData.getAsJsonPrimitive("url").getAsString();
        return new String[]{url, title};
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
