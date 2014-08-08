package nz.co.noirland.noirlandautopromote;

import nz.co.noirland.noirlandautopromote.commands.Command;
import nz.co.noirland.noirlandautopromote.commands.CommandAgree;
import nz.co.noirland.noirlandautopromote.config.PluginConfig;
import nz.co.noirland.noirlandautopromote.database.PromoteDatabase;
import nz.co.noirland.noirlandautopromote.tasks.SaveTimesTask;
import nz.co.noirland.noirlandautopromote.tasks.SortTimesTask;
import nz.co.noirland.zephcore.Debug;
import nz.co.noirland.zephcore.Util;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

public class NoirlandAutoPromote extends JavaPlugin {

    private PluginConfig config;
    private BukkitTask saveTimesTask;
    private static NoirlandAutoPromote inst;
    private static Debug debug;
    private PromoteDatabase db;

    private ArrayList<PlayerTimeData> playerTimeData = new ArrayList<PlayerTimeData>();

    public static NoirlandAutoPromote inst() {
        return inst;
    }

    public static Debug debug() { return debug; }


    @Override
	public void onEnable() {
        inst = this;
        debug = new Debug(this);
        config = PluginConfig.inst();

        db = PromoteDatabase.inst();
        db.checkSchema();

        playerTimeData.addAll(db.getTimeData());
        sortPlayerTimeData();

		for(PlayerTimeData data : playerTimeData) {
			if(Util.player(data.getPlayer()).isOnline()) {
                data.joined();
            }
		}
		startSaveTimes();
        new SortTimesTask(config.getSortTimeSeconds() * 20L);

		this.getCommand("autopromote").setExecutor(new Command());
        this.getCommand("agree").setExecutor((new CommandAgree()));

		getServer().getPluginManager().registerEvents(new PlayerJoinQuitListener(), this);
	}

	@Override
	public void onDisable(){
        saveToDB();
        db.close();
	}

    public ArrayList<PlayerTimeData> getPlayerTimeData() {
        return playerTimeData;
    }

    public PlayerTimeData getTimeData(UUID player) {
        for(PlayerTimeData data : playerTimeData) {
            if(data.getPlayer().equals(player)) {
                return data;
            }
        }
        PlayerTimeData data = new PlayerTimeData(player, 0, 0);
        db.updatePlayerTimes(data);
        playerTimeData.add(data);
        return data;
    }

    public void sortPlayerTimeData() {
        Collections.sort(playerTimeData);
    }

	public void saveToDB() {
        for(PlayerTimeData data : getPlayerTimeData()) {
            if(data.hasChanged()) {
                PromoteDatabase.inst().updatePlayerTimes(data);
            }
        }
	}

	public void reload() {
        saveToDB();
        config.loadFile();
        startSaveTimes();
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
}

