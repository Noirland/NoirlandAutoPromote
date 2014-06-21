package nz.co.noirland.noirlandautopromote.tasks;

import nz.co.noirland.noirlandautopromote.NoirlandAutoPromote;
import nz.co.noirland.noirlandautopromote.PlayerTimeData;
import nz.co.noirland.noirlandautopromote.database.Database;
import org.bukkit.scheduler.BukkitRunnable;

public class SaveTimesTask extends BukkitRunnable {

	private final NoirlandAutoPromote plugin = NoirlandAutoPromote.inst();

	public void run() {
        for(PlayerTimeData data : plugin.getPlayerTimeData()) {
            if(data.hasChanged()) {
                Database.inst().updatePlayerTimes(data, true);
            }
        }
	}
}
