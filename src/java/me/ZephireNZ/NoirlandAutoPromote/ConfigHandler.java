package me.ZephireNZ.NoirlandAutoPromote;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.concurrent.TimeUnit;

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

    public String getDefault() {
        for(String key : plugin.getConfig().getConfigurationSection("ranks").getKeys(false)) {
            if(config.isSet("ranks."+key+".default")) {
                return key;
            }else{
            }
            //return plugin.getConfig().getString("messages."+key+".message").substring(0, 42);
        }
        // return config.getString("defaultPromote.default");
        return null;
    }
}
