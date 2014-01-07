package nz.co.noirland.noirlandautopromote;

import nz.co.noirland.noirlandautopromote.commands.Command;
import nz.co.noirland.noirlandautopromote.commands.CommandAgree;
import nz.co.noirland.noirlandautopromote.config.PluginConfig;
import nz.co.noirland.noirlandautopromote.database.Database;
import nz.co.noirland.noirlandautopromote.tasks.SaveTimesTask;
import nz.co.noirland.noirlandautopromote.util.Debug;
import nz.co.noirland.noirlandautopromote.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.SortedSet;
import java.util.TreeSet;

public class NoirlandAutoPromote extends JavaPlugin {

    private Database db;
    private PluginConfig config;
    private BukkitTask saveTimesTask;
    private static NoirlandAutoPromote inst;

    private SortedSet<PlayerTimeData> playerTimeData = new TreeSet<PlayerTimeData>();

    public static NoirlandAutoPromote inst() {
        return inst;
    }


    @Override
	public void onEnable() {
        inst = this;

        config = PluginConfig.inst();
        db = Database.inst();

        playerTimeData.addAll(db.getTimeData());

		for(PlayerTimeData data : playerTimeData) {
			if(Util.isOnline(data.getPlayer())) {
                data.joined();
            }
		}
		startSaveTimes();

		this.getCommand("autopromote").setExecutor(new Command());
        this.getCommand("agree").setExecutor((new CommandAgree()));

		getServer().getPluginManager().registerEvents(new PlayerJoinQuitListener(), this);
	}

	@Override
	public void onDisable(){
        saveToDB(false);
        db.close();
	}

    public SortedSet<PlayerTimeData> getPlayerTimeData() {
        return playerTimeData;
    }

    public PlayerTimeData getTimeData(String player) {
        for(PlayerTimeData data : playerTimeData) {
            if(data.getPlayer().equalsIgnoreCase(player)) {
                return data;
            }
        }
        PlayerTimeData data = new PlayerTimeData(player, 0, 0);
        db.addPlayer(player, 0, 0);
        playerTimeData.add(data);
        return data;
    }

	public void saveToDB(boolean thread) {
        for(PlayerTimeData data : getPlayerTimeData()) {
            if(Util.isOnline(data.getPlayer()) || data.isChanged()) {
                data.updatePlayTime();
                Database.inst().updatePlayerTimes(data.getPlayer(), data.getPlayTime(), data.getTotalPlayTime(), thread);
                data.setChanged(false);
            }
        }
	}

	public void reload() {
        saveToDB(true);
        db.close();
        db.open();
        config.loadFile();
        startSaveTimes();
	}

	public void debug(String message) {
		if(config.getDebug()) {
			getLogger().info("[DEBUG] " + message);
		}
	}

	public void sendMessage(CommandSender sender, String msg, Boolean prefix) {
        if(prefix) {
            msg = ChatColor.RED + "[NoirPromote] " + ChatColor.RESET + msg;
        }
        if(sender instanceof ConsoleCommandSender) {
            msg = ChatColor.stripColor(msg);
        }
	    sender.sendMessage(msg);
	}

    void startSaveTimes() {
        if(saveTimesTask != null) {
            saveTimesTask.cancel();
        }
        saveTimesTask = new SaveTimesTask().runTaskTimer(this, config.getSaveTimeSeconds() * 20L, config.getSaveTimeSeconds() * 20L);
    }

    /**
     * Disable plugin and show a severe message
     * @param error message to be shown
     */
    public void disable(String error) {
        getLogger().severe(error);
        getPluginLoader().disablePlugin(this);
    }

    /**
     * Disable plugin with severe message and stack trace if debug is enabled.
     * @param error message to be shown
     * @param e execption to be shown
     */
    public void disable(String error, Throwable e) {
        Debug.debug(e);
        disable(error);
    }
}

