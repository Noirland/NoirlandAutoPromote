package me.ZephireNZ.NoirlandAutoPromote;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class NoirlandAutoPromote extends JavaPlugin {
	
	ArrayList<PlayerTimeObject> playerTimeArray = new ArrayList<PlayerTimeObject>();
	DatabaseHandler dbHandler;
	GMHandler gmHandler;
	ConfigHandler confHandler;
	
	@Override
	public void onEnable(){
		PluginDescriptionFile pdfFile = this.getDescription();
		getLogger().info(pdfFile.getName() + " version " + pdfFile.getVersion() + " started.");
		
		dbHandler = new DatabaseHandler(this);
		gmHandler = new GMHandler(this);
		confHandler = new ConfigHandler(this);
		
		for(Player player : getServer().getOnlinePlayers()) {
			PlayerTimeObject pto = new PlayerTimeObject(player); // Add online players to pto object after reload
			pto.setJoinTime();
			playerTimeArray.add(pto);
			
			if(checkForPromotion(player)) {
				promote(player, confHandler.getPromoteTo(gmHandler.getGroup(player))); // Check for promoteable player after reload
			}
		}
		
		new SaveTimesTask(this).runTaskTimer(this, confHandler.getSaveTimeSeconds() * 20L, confHandler.getSaveTimeSeconds() * 20L); // Save times to DB with time in config (in minutes)
		
		//TODO: Commands
		
		getServer().getPluginManager().registerEvents(new PlayerJoinQuitListener(this), this);
		getServer().getPluginManager().registerEvents(gmHandler, this);
	}
	
	@Override
	public void onDisable(){
		saveToDB(); // Save temporary playTimes to Database
		dbHandler.closeConnection();
		
		PluginDescriptionFile pdfFile = this.getDescription();
		getLogger().info(pdfFile.getName() + " version " + pdfFile.getVersion() + " stopped.");
	}
	
	public void saveToDB() {
		for(PlayerTimeObject pto : playerTimeArray) {
			String player = pto.getPlayer().getName();
			pto.setQuitTime();
			dbHandler.setPlayTime(player, pto.getPlayTime() + dbHandler.getPlayTime(player));
			pto.setJoinTime();
			pto.resetQuitTime();
			
		}
	}
	
	public boolean checkForPromotion(Player player) { // Checks if player is eligible for promotion
		String currRank = gmHandler.getGroup(player);
		long playTime = dbHandler.getPlayTime(player.getName());
		if(confHandler.getNoPromote(currRank) == true) { // Don't promote ranks with "noPromote"
			return false;
		}
		if(playTime >= confHandler.getPlayTimeNeededMillis(currRank)) { // Check if player has played enough
			return true;
		}
	return false;
	}
	
	public void promote(Player player, String rank) {
		gmHandler.setGroup(player, rank);
		dbHandler.setPlayTime(player.getName(), 0);
		String prefix = ChatColor.translateAlternateColorCodes("&".charAt(0), gmHandler.getPrefix(player));
		getServer().broadcastMessage(ChatColor.RED + "[AutoPromote] " + ChatColor.RESET + player.getName() + " is now " + prefix + rank + ChatColor.RESET + "!");
	}
}

class SaveTimesTask extends BukkitRunnable {
	
	NoirlandAutoPromote plugin;
	
	public SaveTimesTask(NoirlandAutoPromote plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void run() {
		plugin.saveToDB();
		
	}
}
