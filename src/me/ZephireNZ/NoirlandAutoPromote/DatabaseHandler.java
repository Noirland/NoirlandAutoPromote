package me.ZephireNZ.NoirlandAutoPromote;

import java.sql.ResultSet;
import java.sql.SQLException;

import lib.PatPeter.SQLibrary.SQLite;

public class DatabaseHandler {
	private SQLite SQLite;
	private NoirlandAutoPromote plugin;
	
	public DatabaseHandler(NoirlandAutoPromote plugin) {
		this.plugin = plugin;
		SQLConnect();
		buildTable();
//		try {
//			querySelect = SQLite.prepare("SELECT * FROM playTime WHERE player='?';", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
//			querySelect.
//			
//		} catch (SQLException e) {
//			plugin.getLogger().info(e.getMessage());
//		}
		
	}

	private void SQLConnect() {
		SQLite = new SQLite(plugin.getLogger(), "[AutoPromote]",plugin.getDataFolder().getAbsolutePath(), "players" );
		try {
			SQLite.open();
		}catch(Exception e){
			plugin.getLogger().info(e.getMessage());
			plugin.getPluginLoader().disablePlugin(plugin);
		}
	}
	
	private void buildTable() {
		if(!SQLite.isTable("playTime")) {
			try {
				plugin.getLogger().info("Creating Table");
				SQLite.query("CREATE TABLE playTime (player TEXT, playTime INTEGER);");
			} catch (SQLException e) {
				plugin.getLogger().info(e.getMessage());
				plugin.getPluginLoader().disablePlugin(plugin);
			}
		}
	}
	
	public long getPlayTime(String player) {
		try {
			checkForPlayer(player, 0);
			plugin.getLogger().info("getPlayTime Select");
			ResultSet result = SQLite.query("SELECT * FROM playTime WHERE player='" + player + "';");
			plugin.getLogger().info("getPlayTime getLong");
			long playTime = result.getLong("playTime");
			return playTime;
		} catch (SQLException e) {
			plugin.getLogger().info("getPlayTime Exception");
			plugin.getLogger().info(e.getMessage());
			return 0;
		}
	}
	
	public void setPlayTime(String player, long playTime) {
		try {
			checkForPlayer(player, playTime);
			plugin.getLogger().info("setPlayTime Update");
			SQLite.query("UPDATE playTime SET playTime='" + playTime + "' WHERE player='" + player + "';");
		}catch(SQLException e){
			plugin.getLogger().info("setPlay Time Exception");
			plugin.getLogger().info(e.getMessage());
		}
	}
	
	public void checkForPlayer(String player, long playTime) {
		try {
			plugin.getLogger().info("checkForPlayer Select");
			plugin.getLogger().info("checkForPlayer Select");
			ResultSet countResult = SQLite.query("SELECT COUNT(*) AS count FROM playTime WHERE player='" + player + "';");
			if(countResult.getInt("count") == 0) {
				SQLite.query("INSERT INTO playTime(player, playTime) VALUES('" + player + "', '" + playTime + "');");
			}
			}catch (SQLException e) {
				plugin.getLogger().info("checkForPlayer Exception");
				plugin.getLogger().info(e.getMessage());
		}
	}
	
	public void closeConnection() {
		SQLite.close();
	}
	
	public Object checkResultSet(ResultSet result) {
		
		
		
		return result;
		
	}
}
