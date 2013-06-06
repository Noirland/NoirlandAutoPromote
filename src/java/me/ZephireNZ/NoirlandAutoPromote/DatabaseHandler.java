package me.ZephireNZ.NoirlandAutoPromote;

import lib.PatPeter.SQLibrary.SQLite;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DatabaseHandler {
	private SQLite SQLite;
	private final NoirlandAutoPromote plugin;
	
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
	
	public Map<Integer, String> getRankedList(int startPage) {
		Map<Integer, String> map = new HashMap<Integer, String>();
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
	
	
	public void closeConnection() {
		SQLite.close();
	}
}
