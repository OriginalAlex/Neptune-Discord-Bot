package com.originalalex.github.scrape;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex on 28/08/2017.
 */
public class Scrape {

    private static final String BASE_WIKI_LINK = "https://en.wikipedia.org/wiki/";

    public String getFirstWikiParagraph(String query) {
        List<String> paragraphs = getFirstWikiParagraphs(query, 1);
        return (paragraphs == null) ? null : paragraphs.get(0);
    }

    public List<String> getFirstWikiParagraphs(String query, int number) {
        List<String> paragraphs = new ArrayList<String>();
        try {
            Document doc = Jsoup.connect(BASE_WIKI_LINK + query)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6") // User agent for connections
                    .referrer("http://www.google.com")
                    .get();
            if (doc.text().contains("Wikipedia does not have an article with this exact name")) {
                return null;
            }
            Elements paragraphElements = doc.select("p");
            for (int i = 1; i <= number; i++) { // the first paragraph is not legit
                paragraphs.add(paragraphElements.get(i).text().replaceAll("\\[(\\d{1,3}|citation needed)\\]", ""));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return paragraphs;
    }

}
