package me.ZephireNZ.NoirlandAutoPromote;

import java.util.concurrent.TimeUnit;

public class Util {

    public static String formatTime(long millis) {
        long hour = TimeUnit.HOURS.toMillis(1);
        long day = hour * 24;

        if(millis < hour) {
            long mins = TimeUnit.MILLISECONDS.toMinutes(millis);
            return (mins + " Minutes");
        }
        else if(millis < day) {
            long hours = TimeUnit.MILLISECONDS.toHours(millis);
            return (hours + " Hours");
        }
        else{
            long days = TimeUnit.MILLISECONDS.toDays(millis);
            long subtrDays = TimeUnit.DAYS.toMillis(days);
            long hours = TimeUnit.MILLISECONDS.toHours(millis - subtrDays);
            return (days + " Days, " + hours + " Hours");
        }
    }


}
