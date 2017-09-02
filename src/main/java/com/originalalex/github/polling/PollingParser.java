package com.originalalex.github.polling;

import java.util.ArrayList;
import java.util.List;

public class PollingParser {

    public List<String> parse(String message) { // the first element is the question, every element after are the options
        try {
            List<String> parsed = new ArrayList<>();

            int startIndex = message.indexOf("("); // this signifies the question
            int endIndex = message.indexOf(")");

            String question = message.substring(startIndex + 1, endIndex); // get the question without brackets
            parsed.add(question);

            message = message.substring(endIndex + 1);

            startIndex = message.indexOf("[");
            endIndex = message.indexOf("]");

            while (startIndex != -1) { // repeat while there are still options
                String arg = message.substring(startIndex + 1, endIndex);
                parsed.add(arg);
                message = message.substring(endIndex + 1);

                startIndex = message.indexOf("[");
                endIndex = message.indexOf("]");
            }

            return parsed;
        } catch (Exception e) {
            return null;
        }
    }

}
