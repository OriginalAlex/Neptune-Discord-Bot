package com.originalalex.github.scrape;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Scrape {

    private static final String BASE_WIKI_LINK = "https://en.wikipedia.org/wiki/";

    public String getFirstWikiParagraph(String query) {
        List<String> paragraphs = getFirstWikiParagraphs(query, 1);
        if (paragraphs == null) {
            return null;
        }
        if (paragraphs.get(0) == null) {
            final StringBuilder finalMessage = new StringBuilder("Article on " + query + " not found, perhaps you meant one of the following: \n");
            paragraphs.stream()
                    .filter(e -> e != null)
                    .forEach(e -> finalMessage.append("- " + e + "\n"));
            return finalMessage.toString();
        }
        return paragraphs.get(0); // simply a paragraph
    }

    private List<String> getFirstWikiParagraphs(String query, int number) {
        List<String> paragraphs = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(BASE_WIKI_LINK + query)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6") // User agent for connections
                    .referrer("http://www.google.com")
                    .get();
            if (doc.text().contains("Wikipedia does not have an article with this exact name")) {
                return null;
            }
            Elements paragraphElements = doc.select("p");
            if (paragraphElements.size() == 1) { // The article doesn't exist, but it suggests other articles which are similar to a certain topic.
                String referTo = paragraphElements.get(0).text();
                Element list = doc.select("ul").first(); // get the unordered list of the suggested
                Elements items = list.select("li"); // individual items.
                paragraphs.add(null); // This will signify that it is a list and not a paragraph!
                for (Element e : items) {
                    paragraphs.add(e.text());
                }
            }
            for (int i = 0; i < number && i < paragraphElements.size(); i++) { // the first paragraph is not legit
                Element el = paragraphElements.get(i);
                if (el.parent().parent().tagName().equalsIgnoreCase("tr")) {
                    number++; // do it for one more iteration [IN THIS CASE IT IS ACTUALLY GRABBING THE SIDEBAR AND NOT THE PARAGRAPHS FILLED WITH INFORMATION
                } else {
                    paragraphs.add(paragraphElements.get(i).text().replaceAll("\\[(\\d{1,3}|citation needed)\\]", ""));
                }
            }
            return paragraphs;
        } catch (IOException e) {
            return null;
        }
    }

}
