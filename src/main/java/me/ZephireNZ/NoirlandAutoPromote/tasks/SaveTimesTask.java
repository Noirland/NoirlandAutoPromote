package me.ZephireNZ.NoirlandAutoPromote.tasks;

import me.ZephireNZ.NoirlandAutoPromote.NoirlandAutoPromote;
import me.ZephireNZ.NoirlandAutoPromote.PlayerTimeData;
import me.ZephireNZ.NoirlandAutoPromote.database.Database;
import org.bukkit.scheduler.BukkitRunnable;

public class SaveTimesTask extends BukkitRunnable {

	private final NoirlandAutoPromote plugin = NoirlandAutoPromote.inst();

	public void run() {
        for(PlayerTimeData data : plugin.getPlayerTimeData()) {
            if(data.isOnline()) {
                data.updatePlayTime();
                Database.inst().updatePlayerTimes(data.getPlayer(), data.getPlayTime(), data.getTotalPlayTime(), true);
                data.setChanged(false);
            }
        }
	}
}
