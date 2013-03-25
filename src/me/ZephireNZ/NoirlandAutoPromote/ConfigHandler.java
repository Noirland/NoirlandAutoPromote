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
		plugin.debug("ConfigHandler saveConfig");
		plugin.saveConfig();
	}
	
	public FileConfiguration getConfig() {
		plugin.debug("ConfigHandler getConfig");
		return config;
	}
	
	public String getPromoteTo(String rank) {
		plugin.debug("ConfigHandler getPromoteTo: " + config.getString("ranks." + rank + ".promoteTo"));
		return config.getString("ranks." + rank + ".promoteTo");
	}
	
	public boolean getNoPromote(String rank) {
		plugin.debug("ConfigHandler getNoPromote: " + config.getBoolean("ranks." + rank + ".noPromote"));
		return config.getBoolean("ranks." + rank + ".noPromote");
	}
	
	public long getPlayTimeNeededMillis(String rank) {
		int playTimeNeededHours = config.getInt("ranks." + rank + ".playTimeNeeded");
		plugin.debug("ConfigHandler getPlayTimeNeededMillis Hours: " + playTimeNeededHours);
		plugin.debug("ConfigHandler getPlayTimeNeededMillis: " + TimeUnit.MILLISECONDS.convert(playTimeNeededHours, TimeUnit.HOURS));
		return TimeUnit.MILLISECONDS.convert(playTimeNeededHours, TimeUnit.HOURS);
	}
	
	public long getSaveTimeSeconds() {
		plugin.debug("ConfigHandler getSaveTimeSeconds: " + TimeUnit.SECONDS.convert(config.getInt("settings.saveTime"),TimeUnit.MINUTES));
		return TimeUnit.SECONDS.convert(config.getInt("settings.saveTime"),TimeUnit.MINUTES);
	}
	
	public boolean getDebug() {
		return config.getBoolean("settings.debug");
	}
}
