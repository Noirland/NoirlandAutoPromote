package nz.co.noirland.noirlandautopromote;

import nz.co.noirland.zephcore.Config;
import nz.co.noirland.zephcore.Debug;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.TimeUnit;

public class PromoteConfig extends Config {

    private static PromoteConfig inst;

    @Override
    protected Plugin getPlugin() {
        return NoirlandAutoPromote.inst();
    }

    @Override
    protected Debug getDebug() {
        return NoirlandAutoPromote.debug();
    }

    private PromoteConfig() {
        super("config.yml");
    }

    public static PromoteConfig inst() {
        if(inst == null) {
            inst = new PromoteConfig();
        }

        return inst;
    }

    public void reload() {
        load();
    }

    public String  getDatabase() { return config.getString("mysql.database"); }
    public String  getUsername() { return config.getString("mysql.username"); }
    public String  getPassword() { return config.getString("mysql.password"); }
    public int     getPort()     { return config.getInt   ("mysql.port", 3306); }
    public String  getHost()     { return config.getString("mysql.host", "localhost"); }
    public String  getPrefix()   { return config.getString("mysql.prefix", "promote"); }

    public String getPromoteTo(String rank)  { return config.getString("ranks." + rank.toLowerCase() + ".promoteTo"); }
    public boolean getNoPromote(String rank) { return config.getBoolean("ranks." + rank.toLowerCase() + ".noPromote"); }

    public long getSaveTimeSeconds() { return TimeUnit.MINUTES.toSeconds(config.getInt("settings.saveTime")); }
    public long getSortTimeSeconds() { return TimeUnit.MINUTES.toSeconds(config.getInt("settings.sortTime", 5)); }

    public long getPlayTimeNeededMillis(String rank) {
        int playTimeNeededHours = config.getInt("ranks." + rank.toLowerCase() + ".playTimeNeeded");
        return TimeUnit.MILLISECONDS.convert(playTimeNeededHours, TimeUnit.HOURS);
    }

    public String getDefault() {
        for(String key : config.getConfigurationSection("ranks").getKeys(false)) {
            if(config.isSet("ranks."+key+".default")) {
                return key;
            }
        }
        return null;
    }


}
