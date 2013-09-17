package me.ZephireNZ.NoirlandAutoPromote.tasks;

import me.ZephireNZ.NoirlandAutoPromote.NoirlandAutoPromote;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SaveTimesTask extends BukkitRunnable {

	private final NoirlandAutoPromote plugin;

	public SaveTimesTask(NoirlandAutoPromote plugin) {
		this.plugin = plugin;
	}

	public void run() {
		for(Player player : plugin.getServer().getOnlinePlayers()) { // Check for promoteable player
			plugin.pmHandler.checkForPromotion(player);
            //TODO: Add running of task with time to promotion less than the saveTime's interval

        }


        plugin.saveToDB(true); // Save all players to DB on event
	}
}
