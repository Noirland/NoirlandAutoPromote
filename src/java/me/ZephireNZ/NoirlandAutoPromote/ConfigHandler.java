package me.ZephireNZ.NoirlandAutoPromote;

import java.util.concurrent.TimeUnit;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigHandler {
	NoirlandAutoPromote plugin;
	FileConfiguration config;
	
	public ConfigHandler(NoirlandAutoPromote plugin) {
		this.plugin = plugin;
	        plugin.saveDefaultConfig();
	        loadConfig();
	        config = plugin.getConfig();
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
		String promoteTo = config.getString("ranks." + rank.toLowerCase() + ".promoteTo");
		return promoteTo;
	}
	
	public boolean getNoPromote(String rank) {
		Boolean noPromote = config.getBoolean("ranks." + rank.toLowerCase() + ".noPromote");
		return noPromote;
	}
	
	public long getPlayTimeNeededMillis(String rank) {
		int playTimeNeededHours = config.getInt("ranks." + rank.toLowerCase() + ".playTimeNeeded");
		long playTimeNeededMillis = TimeUnit.MILLISECONDS.convert(playTimeNeededHours, TimeUnit.HOURS);
		return playTimeNeededMillis;
	}
	
	public long getSaveTimeSeconds() {
		long saveTimeSeconds = TimeUnit.SECONDS.convert(config.getInt("settings.saveTime"),TimeUnit.MINUTES);
		return saveTimeSeconds;
	}
	
	public boolean getDebug() {
		return config.getBoolean("settings.debug".toLowerCase());
	}
}
