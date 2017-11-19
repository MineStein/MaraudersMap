package me.tylergrissom.maraudersmap.utility;

import me.tylergrissom.maraudersmap.Main;

/**
 * Copyright (c) 2013-2016 Tyler Grissom
 */
public class TimeUtility {

    private Main plugin;

    public TimeUtility(Main plugin) {
        this.plugin = plugin;
    }

    public String format(int seconds) {
        int hours = seconds / 3600;
        int remainder = seconds % 3600;
        int mins = remainder / 60;
        int secs = remainder % 60;

        if (hours > 0) {
            return hours + "h " + mins + "m " + secs + "s";
        } else if (mins > 0) {
            return mins + "m " + secs + "s";
        } else {
            return secs + "s";
        }
    }
}
