package me.ZephireNZ.NoirlandAutoPromote;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class NoirlandAutoPromote extends JavaPlugin {
	
	ArrayList<PlayerTimeObject> playerTimeArray = new ArrayList<PlayerTimeObject>();
	DatabaseHandler dbHandler;
	GMHandler gmHandler;
	
	@Override
	public void onEnable(){
		PluginDescriptionFile pdfFile = this.getDescription();
		getLogger().info(pdfFile.getName() + " version " + pdfFile.getVersion() + " started.");
		
		dbHandler = new DatabaseHandler(this);
		gmHandler = new GMHandler(this);
		
		for(Player player : getServer().getOnlinePlayers()) {
			PlayerTimeObject pto = new PlayerTimeObject(player);
			pto.setJoinTime();
			playerTimeArray.add(pto);
		}
		
		// TODO: Run Async task saveToDB() (possibly with time from config file)
		
		//TODO: Commands
		
		getServer().getPluginManager().registerEvents(new PlayerJoinQuitListener(this), this);
		getServer().getPluginManager().registerEvents(gmHandler, this);
	}
	
	@Override
	public void onDisable(){
		saveToDB();
		dbHandler.closeConnection();
		
		PluginDescriptionFile pdfFile = this.getDescription();
		getLogger().info(pdfFile.getName() + " version " + pdfFile.getVersion() + " stopped.");
	}
	
	public void saveToDB() {
		for(PlayerTimeObject pto : playerTimeArray) {
			getLogger().info(pto.getPlayer().getName() + "Join: " + pto.getJoinTime());
			String player = pto.getPlayer().getName();
			pto.setQuitTime();
			dbHandler.setPlayTime(player, pto.getPlayTime() + dbHandler.getPlayTime(player));
			pto.setJoinTime();
			pto.resetQuitTime();
			getLogger().info(pto.getPlayer().getName() + "Join: " + pto.getJoinTime());
			getLogger().info(pto.getPlayer().getName() + "Quit: " + pto.getQuitTime());
			
		}
	}
}
