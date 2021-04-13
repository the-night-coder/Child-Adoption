package com.nightcoder.mothercare.Supports;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Time {

    public static String getTimeFullText(double dataDate) {
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        String time = dateFormat1.format(dataDate);

        String timeAgo = null;

        String suffix = "ago";

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
            Date pasTime = dateFormat.parse(time);

            Date nowTime = new Date();
            assert pasTime != null;
            long dateDiff = nowTime.getTime() - pasTime.getTime();

            long second = TimeUnit.MILLISECONDS.toSeconds(dateDiff);
            long minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff);
            long hour = TimeUnit.MILLISECONDS.toHours(dateDiff);
            long day = TimeUnit.MILLISECONDS.toDays(dateDiff);

            if (second < 60) {
                timeAgo = "now";
            } else if (minute < 60) {
                timeAgo = minute + "m " + suffix;
            } else if (hour < 24) {
                timeAgo = hour + "h " + suffix;
            } else if (day >= 7) {
                if (day > 360) {
                    timeAgo = (day / 30) + "y " + suffix;
                } else if (day > 30) {
                    timeAgo = (day / 360) + "m " + suffix;
                } else {
                    timeAgo = (day / 7) + "w " + suffix;
                }
            } else {
                if (day == 1) {
                    timeAgo = day + "d " + suffix;
                } else {
                    timeAgo = day + "d " + suffix;
                }

            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeAgo;
    }

}
