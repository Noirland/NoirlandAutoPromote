package nz.co.noirland.noirlandautopromote.callbacks;

import nz.co.noirland.noirlandautopromote.GMHandler;
import nz.co.noirland.noirlandautopromote.NoirlandAutoPromote;
import nz.co.noirland.noirlandautopromote.PlayerTimeData;
import nz.co.noirland.noirlandautopromote.PromotionHandler;
import nz.co.noirland.noirlandautopromote.config.PluginConfig;
import nz.co.noirland.zephcore.Callback;
import nz.co.noirland.zephcore.Util;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.UUID;

public class InfoCallback extends Callback {

    UUID uuid;
    String name;
    CommandSender sender;


    public InfoCallback(CommandSender sender, String player) {
        super(NoirlandAutoPromote.inst());
        this.sender = sender;
        new InfoThread(player);
    }

    @Override
    public void run() {
        NoirlandAutoPromote plugin = NoirlandAutoPromote.inst();
        GMHandler gmHandler = GMHandler.inst();
        PluginConfig config = PluginConfig.inst();

        if(uuid == null) {
            plugin.sendMessage(sender, "That player has not played on this server.", true);
            return;
        }

        OfflinePlayer oPlayer = Util.player(uuid);

        if(!oPlayer.hasPlayedBefore() && !oPlayer.isOnline()) {
            plugin.sendMessage(sender, "That player has not played on this server.", true);
            return;
        }

        if(oPlayer.isOnline()) {
            PromotionHandler.inst().checkForPromotion(oPlayer.getPlayer());
        }

        PlayerTimeData data = plugin.getTimeData(uuid);
        String group = gmHandler.getGroup(name);

        long neededMillis = config.getPlayTimeNeededMillis(group) - data.getPlayTime();

        plugin.sendMessage(sender,"==== " + ChatColor.RED + "NoirPromote" + ChatColor.RESET + " ====", false);

        if(!config.getNoPromote(group)){
            String nextColor = gmHandler.getColor(name, true);
            String nextRank = config.getPromoteTo(group);
            plugin.sendMessage(sender,"Time until " + nextColor + nextRank + ChatColor.RESET + ": " + Util.formatTime(neededMillis), false);
        }

        ArrayList<PlayerTimeData> set = plugin.getPlayerTimeData();
        int rank = set.indexOf(data) + 1;
        plugin.sendMessage(sender,"Total Play Time: " + Util.formatTime(data.getTotalPlayTime()) + ChatColor.DARK_GRAY + " (#" + rank + ")", false);
    }

    private class InfoThread implements Runnable {

        private String p;

        private InfoThread(String player) {
            this.p = player;
            new Thread(this, "AutoPromote-InfoThread").start();
        }

        @Override
        public void run() {
            uuid = Util.uuid(p);
            if(uuid != null) {
                name = Util.name(uuid); // Proper capitalisation
            }
            schedule();
        }
    }

}
