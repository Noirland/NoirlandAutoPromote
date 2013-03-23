package me.ZephireNZ.NoirlandAutoPromote;

import java.util.ArrayList;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class NoirlandAutoPromote extends JavaPlugin {
	
	ArrayList<PlayerTimeObject> PlayerPlayTimerArray = new ArrayList<PlayerTimeObject>();
	
	@Override
	public void onDisable(){
		PluginDescriptionFile pdfFile = this.getDescription();
		getLogger().info(pdfFile.getName() + " version " + pdfFile.getVersion() + " stopped.");
	}
	
	@Override
	public void onEnable(){
		getServer().getPluginManager().registerEvents(new PlayerJoinQuitListener(this), this);
		
		PluginDescriptionFile pdfFile = this.getDescription();
		getLogger().info(pdfFile.getName() + " version " + pdfFile.getVersion() + " started.");
	}
}
