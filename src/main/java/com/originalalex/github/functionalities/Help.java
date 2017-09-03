package com.originalalex.github.functionalities;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Help implements Function {

    // Section of README.md to read from.
    private static final String SECTION = "Current Commands:";

    private String message;
    private List<String> sendOrder;

    public Help() {
        // Can we read from the cache?

        // Get README.md (use the raw text file)
        URL url;
        try {
            url = new URL("https://raw.githubusercontent.com/OriginalAlex/DiscordBot/master/README.md");
            Scanner scanner = new Scanner(url.openStream());

            StringBuilder sb = new StringBuilder();

            boolean read = false;

            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                // Read from the "Current Commands" section
                if (!read) {
                    if (line.contains(SECTION)) {
                        read = true;
                    } else {
                        continue;
                    }
                }

                // Read for the next section so we don't pull more than we need
                // to
                if (line.contains("##") && !line.contains(SECTION)) {
                    read = false;
                    break;
                }

                // Otherwise, add to the output text
                sb.append(line + "\n");
            }
            message = sb.toString();
            this.sendOrder = divideStringIntoSubStringsOf2000Chars(message);
            scanner.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private List<String> divideStringIntoSubStringsOf2000Chars(String str) { // the messages must be < 2000 characters
        List<String> messages = new ArrayList<>();
        String temp = "";
        String[] lines = str.split("\n");
        for (String line : lines) {
            if (temp.length() + line.length() >= 2000) {
                messages.add(temp);
                temp = "";
            }
            temp += line;
            temp += "\n";
        }
        messages.add(temp.substring(0, temp.lastIndexOf("\n")));
        return messages;
    }

    @Override
    public void handle(MessageReceivedEvent e, String[] parts) {
        e.getAuthor().openPrivateChannel().queue(channel -> sendOrder.forEach(m -> channel.sendMessage(m).queue()));
    }

}