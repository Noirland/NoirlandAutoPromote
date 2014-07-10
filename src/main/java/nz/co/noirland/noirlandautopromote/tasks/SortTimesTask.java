package nz.co.noirland.noirlandautopromote.tasks;

import nz.co.noirland.noirlandautopromote.NoirlandAutoPromote;
import org.bukkit.scheduler.BukkitRunnable;

public class SortTimesTask extends BukkitRunnable {

    public SortTimesTask(long period) {
        this.runTaskTimer(NoirlandAutoPromote.inst(), 0, period); // Sort every 5 mins
    }

    @Override
    public void run() {
        NoirlandAutoPromote.inst().sortPlayerTimeData();
    }
}
