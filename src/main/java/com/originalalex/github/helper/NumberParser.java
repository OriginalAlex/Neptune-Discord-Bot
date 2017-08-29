package com.originalalex.github.helper;

public class NumberParser {

    public int parse(String potential) {
        try {
            return Integer.parseInt(potential);
        } catch (NumberFormatException e) {
            return Integer.MIN_VALUE;
        }
    }
}
