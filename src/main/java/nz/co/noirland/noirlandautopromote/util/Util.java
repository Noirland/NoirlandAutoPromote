package nz.co.noirland.noirlandautopromote.util;

import nz.co.noirland.noirlandautopromote.NoirlandAutoPromote;
import nz.co.noirland.noirlandautopromote.PlayerTimeData;
import org.bukkit.OfflinePlayer;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
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

    public static boolean isOnline(String player) {
        OfflinePlayer off = NoirlandAutoPromote.inst().getServer().getOfflinePlayer(player);
        return off.isOnline();
    }

    public static TreeMap<Integer, PlayerTimeData> getSortedMap(Set<PlayerTimeData> set) {
        TreeMap<Integer, PlayerTimeData> map = new TreeMap<Integer, PlayerTimeData>();
        Iterator<PlayerTimeData> it = set.iterator();
        int i = 1;
        while(it.hasNext()) {
            map.put(i++, it.next());
        }
        return map;
    }
}
