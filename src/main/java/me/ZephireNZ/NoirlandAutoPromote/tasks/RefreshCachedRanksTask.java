package me.ZephireNZ.NoirlandAutoPromote.tasks;

import lib.PatPeter.SQLibrary.SQLite;
import me.ZephireNZ.NoirlandAutoPromote.NoirlandAutoPromote;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class RefreshCachedRanksTask extends BukkitRunnable {

    private final NoirlandAutoPromote plugin;
    private final lib.PatPeter.SQLibrary.SQLite SQLite;
    private final Map<Integer, String> tempMap = new HashMap<Integer, String>();
    private final Map<Integer, String> map;

    public RefreshCachedRanksTask(NoirlandAutoPromote plugin, SQLite SQLite, Map<Integer, String> map) {
        this.SQLite = SQLite;
        this.map = map;
        this.plugin = plugin;

    }

    public void run() {
        try {
            int i = 1;
            tempMap.clear();
            ResultSet result = SQLite.query("SELECT * FROM playTime ORDER BY totalPlayTime DESC");

            while(result.next()) {

                tempMap.put(i, result.getString("player"));
                i++;

            }
            map.clear();
            map.putAll(tempMap);
            plugin.debug("Rank cache refreshed successfully");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
