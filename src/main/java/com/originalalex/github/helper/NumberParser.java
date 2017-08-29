package com.originalalex.github.helper;

/**
 * Created by Alex on 29/08/2017.
 */
public class NumberParser {

    public int parse(String potential) {
        try {
            int val = Integer.parseInt(potential);
            return val;
        } catch (NumberFormatException e) {
            return Integer.MIN_VALUE;
        }
    }
}
