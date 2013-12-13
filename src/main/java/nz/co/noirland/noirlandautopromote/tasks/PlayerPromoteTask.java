package nz.co.noirland.noirlandautopromote.tasks;

import nz.co.noirland.noirlandautopromote.GMHandler;
import nz.co.noirland.noirlandautopromote.NoirlandAutoPromote;
import nz.co.noirland.noirlandautopromote.PlayerTimeData;
import nz.co.noirland.noirlandautopromote.PromotionHandler;
import nz.co.noirland.noirlandautopromote.config.PluginConfig;
import nz.co.noirland.noirlandautopromote.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.TimeUnit;

public class PlayerPromoteTask extends BukkitRunnable {

    private final NoirlandAutoPromote plugin = NoirlandAutoPromote.inst();
    private final PromotionHandler pmHandler = PromotionHandler.inst();
    private final GMHandler gmHandler = GMHandler.inst();
    private final PluginConfig config = PluginConfig.inst();
    private PlayerTimeData data;

    public PlayerPromoteTask(String player) {
        if(!Util.isOnline(player)) {
            return;
        }
        Player p = plugin.getServer().getPlayer(player);
        this.data = plugin.getTimeData(player);
        String group = gmHandler.getGroup(player);
        long needed = config.getPlayTimeNeededMillis(group);
        long left = needed - data.getPlayTime();
        if(left <= 0) {
            pmHandler.checkForPromotion(p);
            return;
        }
        if(left <= Util.DAY) {
            plugin.sendMessage(p, "You have " + Util.formatTime(left) + " until " + gmHandler.getColor(p, true) + config.getPromoteTo(group) + ChatColor.RESET + "!", true);
            runTaskLater(plugin, TimeUnit.MILLISECONDS.toSeconds(left) + 5 * 20L);
            data.setPromoteTask(this);
        }
    }

    @Override
    public void run() {
        if(!Util.isOnline(data.getPlayer())) return;
        Player player = plugin.getServer().getPlayer(data.getPlayer());
        String group = gmHandler.getGroup(data.getPlayer());
        long needed = config.getPlayTimeNeededMillis(group);
        long left = needed - data.getPlayTime();
        if(left <= 0) {
            pmHandler.checkForPromotion(player);
            data.setPromoteTask(null);
            return;
        }
        data.setPromoteTask(new PlayerPromoteTask(data.getPlayer()));
    }
}
