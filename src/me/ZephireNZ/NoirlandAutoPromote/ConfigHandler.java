package me.ZephireNZ.NoirlandAutoPromote;

import java.util.concurrent.TimeUnit;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigHandler {
	NoirlandAutoPromote plugin;
	FileConfiguration config;
	
	public ConfigHandler(NoirlandAutoPromote plugin) {
		this.plugin = plugin;
	        plugin.saveDefaultConfig();
	        config = plugin.getConfig();
	        loadConfig();
	}
	
	public void loadConfig() {
			plugin.reloadConfig();
	}
	
	public void saveConfig() {
		plugin.saveConfig();
	}
	
	public FileConfiguration getConfig() {
		return config;
	}
	
	public String getPromoteTo(String rank) {
		return config.getString("ranks." + rank + ".promoteTo");
	}
	
	public boolean getNoPromote(String rank) {
		return config.getBoolean("ranks." + rank + ".noPromote");
	}
	
	public long getPlayTimeNeededMillis(String rank) {
		int playTimeNeededHours = config.getInt("ranks." + rank + ".playTimeNeeded");
		return TimeUnit.MILLISECONDS.convert(playTimeNeededHours, TimeUnit.HOURS);
	}
	
	public long getSaveTimeSeconds() {
		return TimeUnit.SECONDS.convert(config.getInt("settings.saveTime"),TimeUnit.MINUTES);
	}
}
