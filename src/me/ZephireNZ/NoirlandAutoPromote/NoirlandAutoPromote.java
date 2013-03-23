package me.ZephireNZ.NoirlandAutoPromote;

import java.util.ArrayList;

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
		
		
		getServer().getPluginManager().registerEvents(new PlayerJoinQuitListener(this), this);
	}
	
	@Override
	public void onDisable(){
		dbHandler.closeConnection();
		
		PluginDescriptionFile pdfFile = this.getDescription();
		getLogger().info(pdfFile.getName() + " version " + pdfFile.getVersion() + " stopped.");
	}
}
