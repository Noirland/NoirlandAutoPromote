package me.ZephireNZ.NoirlandAutoPromote;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.concurrent.TimeUnit;

public class ConfigHandler {
	private final NoirlandAutoPromote plugin;
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
        return config.getString("ranks." + rank.toLowerCase() + ".promoteTo");
	}
	
	public boolean getNoPromote(String rank) {
        return config.getBoolean("ranks." + rank.toLowerCase() + ".noPromote");
	}
	
	public long getPlayTimeNeededMillis(String rank) {
		int playTimeNeededHours = config.getInt("ranks." + rank.toLowerCase() + ".playTimeNeeded");
        return TimeUnit.MILLISECONDS.convert(playTimeNeededHours, TimeUnit.HOURS);
	}
	
	public long getSaveTimeSeconds() {
        return TimeUnit.SECONDS.convert(config.getInt("settings.saveTime"),TimeUnit.MINUTES);
	}
	
	public boolean getDebug() {
		return config.getBoolean("settings.debug".toLowerCase());
	}

    public String getDefault() {
        for(String key : plugin.getConfig().getConfigurationSection("ranks").getKeys(false)) {
            if(config.isSet("ranks."+key+".default")) {
                return key;
            }
        }
        return null;
    }
}