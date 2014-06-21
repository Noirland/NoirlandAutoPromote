package nz.co.noirland.noirlandautopromote.tasks;

import nz.co.noirland.noirlandautopromote.GMHandler;
import nz.co.noirland.noirlandautopromote.NoirlandAutoPromote;
import nz.co.noirland.noirlandautopromote.PlayerTimeData;
import nz.co.noirland.noirlandautopromote.PromotionHandler;
import nz.co.noirland.noirlandautopromote.config.PluginConfig;
import nz.co.noirland.zephcore.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class PlayerPromoteTask extends BukkitRunnable {

    private final PromotionHandler pmHandler = PromotionHandler.inst();
    private final GMHandler gmHandler = GMHandler.inst();
    private final PluginConfig config = PluginConfig.inst();
    private PlayerTimeData data;

    public PlayerPromoteTask(UUID player) {
        if(!Util.player(player).isOnline()) {
            return;
        }
        NoirlandAutoPromote plugin = NoirlandAutoPromote.inst();
        Player p = Util.player(player).getPlayer();
        this.data = plugin.getTimeData(player);
        String group = gmHandler.getGroup(Util.player(player).getName());
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
        if(!Util.player(data.getPlayer()).isOnline()) return;
        Player player = Bukkit.getPlayer(data.getPlayer());
        String group = gmHandler.getGroup(Util.player(data.getPlayer()).getName());
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
