package com.originalalex.github.helper;

public class ReadableTime {

    public static String convertSeconds(int seconds) { // Converts seconds to String of format hh:mm:ss
        int hours = seconds / 3600;
        int remaingSeconds = seconds % 3600;
        int minutes = remaingSeconds / 60;
        int secondsLeft = remaingSeconds % 60;
        String remHours = (hours < 10 ? "0" : "") + hours;
        String remMinutes = (minutes < 10 ? "0" : "") + minutes;
        String remSeconds = (secondsLeft < 10 ? "0" : "") + secondsLeft;
        return remHours + ":" + remMinutes + ":" + remSeconds;
    }

}
