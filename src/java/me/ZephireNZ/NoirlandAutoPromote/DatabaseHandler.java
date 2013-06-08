package me.ZephireNZ.NoirlandAutoPromote;

import lib.PatPeter.SQLibrary.SQLite;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class DatabaseHandler {
	private SQLite SQLite;
	private final NoirlandAutoPromote plugin;
    final Map<Integer, String> cachedRanks = new HashMap<Integer, String>();
	
	public DatabaseHandler(NoirlandAutoPromote plugin) {
		this.plugin = plugin;
		SQLConnect();
		checkTable();
	}

	public void SQLConnect() {
		SQLite = new SQLite(plugin.getLogger(), "[AutoPromote]", plugin.getDataFolder().getAbsolutePath(), "players" );
		try {
			SQLite.open();
		}catch(Exception e){
			plugin.getLogger().severe(e.getMessage());
			plugin.getPluginLoader().disablePlugin(plugin);
		}
	}
	
	private void checkTable() {
		try {
			if(!SQLite.isTable("playTime")) {
					plugin.getLogger().info("Creating Table");
					SQLite.query("CREATE TABLE playTime (player TEXT, playTime INTEGER, totalPlayTime INTEGER);");
			}
			ResultSet result = SQLite.query("PRAGMA table_info(playTime);");
			boolean hasTotal = false;
			for(; result.next();) {
				if(result.getString(2).equals("totalPlayTime")) {
					hasTotal = true;
				}
			}
			if(!hasTotal) {
				SQLite.query("ALTER TABLE playTime ADD COLUMN totalPlayTime INTEGER;");
			}
		} catch (SQLException e) {
			plugin.getLogger().severe(e.getMessage());
			e.printStackTrace();
			plugin.getPluginLoader().disablePlugin(plugin);
		}
	}
	
	public void checkForPlayer(String player) {
		player = player.toLowerCase();
		try {
			ResultSet countResult = SQLite.query("SELECT COUNT(*) AS count FROM playTime WHERE player='" + player + "';");
			ResultSet result = SQLite.query("SELECT * FROM playTime WHERE player='" + player + "';");
			if(countResult.getInt("count") == 0) {
				SQLite.query("INSERT INTO playTime(player, playTime, totalPlayTime) VALUES('" + player + "', '0', '0');");
			}
			else if(result.getInt("totalPlayTime") == 0){
				ResultSet playTimeResult = SQLite.query("SELECT * FROM playTime WHERE player='" + player + "';");
				long playTime = playTimeResult.getLong("playTime");
				SQLite.query("UPDATE playTime SET totalPlayTime='" + playTime + "' WHERE player='" + player + "';");
			}
			}catch (SQLException e) {
				plugin.getLogger().severe(e.getMessage());
		}
	}
	
	public long getPlayTime(String player) {
		try {
			player = player.toLowerCase();
			checkForPlayer(player);
			ResultSet result = SQLite.query("SELECT * FROM playTime WHERE player='" + player + "';");
            return result.getLong("playTime");
		} catch (SQLException e) {
			plugin.getLogger().severe(e.getMessage());
			return 0;
		}
	}
	
	public void setPlayTime(String player, long playTime) {
		player = player.toLowerCase();
		try {
			checkForPlayer(player);
			SQLite.query("UPDATE playTime SET playTime='" + playTime + "' WHERE player='" + player + "';");
		}catch(SQLException e){
			plugin.getLogger().severe(e.getMessage());
		}
	}
	
	public long getTotalPlayTime(String player) {
		try {
			player = player.toLowerCase();
			checkForPlayer(player);
			ResultSet result = SQLite.query("SELECT * FROM playTime WHERE player='" + player + "';");
            return result.getLong("totalPlayTime");
		} catch (SQLException e) {
			plugin.getLogger().severe(e.getMessage());
			return 0;
		}
	}
	
	public void setTotalPlayTime(String player, long totalPlayTime) {
		player = player.toLowerCase();
		try {
			checkForPlayer(player);
			SQLite.query("UPDATE playTime SET totalPlayTime='" + totalPlayTime + "' WHERE player='" + player + "';");
		}catch(SQLException e){
			plugin.getLogger().severe(e.getMessage());
		}
	}
	
	public TreeMap<Integer, String> getRankedList(int startPage) {
		TreeMap<Integer, String> map = new TreeMap<Integer, String>();
		try {
            int StartNum = ((startPage-1)*10);
			ResultSet result = SQLite.query("SELECT * FROM playTime ORDER BY totalPlayTime DESC LIMIT 10 OFFSET " + StartNum + ";");

			for(int i = 1; i <=10;i++) {
				if(result.next()) {
					if(result.getString("player") != null) {
						map.put(StartNum + (i), result.getString("player"));
					}
				}else{
					return map;
				}
			}
			return map;
		} catch (SQLException e) {
			plugin.getLogger().severe(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

    public void refreshCachedRanks() {

        new RefreshCachedRanks(plugin, SQLite, cachedRanks).runTask(plugin);
    }

    public int getPlayerRank(String player) {
        if(!cachedRanks.isEmpty()) {
            for(Map.Entry<Integer, String> entry : cachedRanks.entrySet()) {
                if(entry.getValue().equalsIgnoreCase(player)) {
                    return entry.getKey();
                }
            }
        }
        return 0;
    }

	public void closeConnection() {
		SQLite.close();
	}


}

class RefreshCachedRanks extends BukkitRunnable {

    private final NoirlandAutoPromote plugin;
    private final SQLite SQLite;
    private final Map<Integer, String> tempMap = new HashMap<Integer, String>();
    private final Map<Integer, String> map;

    public RefreshCachedRanks(NoirlandAutoPromote plugin, SQLite SQLite, Map<Integer, String> map) {
        this.SQLite = SQLite;
        this.map = map;
        this.plugin = plugin;

    }

    @Override
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
