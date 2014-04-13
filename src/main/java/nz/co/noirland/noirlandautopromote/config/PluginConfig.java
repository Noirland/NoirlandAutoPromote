package nz.co.noirland.noirlandautopromote.config;

import java.util.concurrent.TimeUnit;

public class PluginConfig extends Config {

    private static PluginConfig inst;

    private PluginConfig() {
        super("config.yml");
    }

    public static PluginConfig inst() {
        if(inst == null) {
            inst = new PluginConfig();
        }

        return inst;
    }

    public String  getDatabase() { return config.getString("mysql.database"); }
    public String  getUsername() { return config.getString("mysql.username"); }
    public String  getPassword() { return config.getString("mysql.password"); }
    public int     getPort()     { return config.getInt   ("mysql.port", 3306); }
    public String  getHost()     { return config.getString("mysql.host", "localhost"); }
    public String  getPrefix()   { return config.getString("mysql.prefix", "promote_"); }

    public boolean getDebug()    { return config.getBoolean("noirstore.debug", false);}

    public String getPromoteTo(String rank)  { return config.getString("ranks." + rank.toLowerCase() + ".promoteTo"); }
    public boolean getNoPromote(String rank) { return config.getBoolean("ranks." + rank.toLowerCase() + ".noPromote"); }
    public long getSaveTimeSeconds() { return TimeUnit.SECONDS.convert(config.getInt("settings.saveTime"),TimeUnit.MINUTES); }

    public long getPlayTimeNeededMillis(String rank) {
        int playTimeNeededHours = config.getInt("ranks." + rank.toLowerCase() + ".playTimeNeeded");
        return TimeUnit.MILLISECONDS.convert(playTimeNeededHours, TimeUnit.HOURS);
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
