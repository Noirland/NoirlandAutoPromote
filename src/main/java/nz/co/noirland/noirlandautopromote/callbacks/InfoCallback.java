package nz.co.noirland.noirlandautopromote.callbacks;

import com.google.common.collect.Maps;
import com.google.common.util.concurrent.FutureCallback;
import nz.co.noirland.noirlandautopromote.*;
import nz.co.noirland.zephcore.Callback;
import nz.co.noirland.zephcore.Util;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;

public class InfoCallback extends Callback<Map.Entry<UUID, String>> {

    public InfoCallback(CommandSender sender, String name) {
        super(new InfoTask(name), new InfoResult(sender));
    }

}

class InfoTask implements Callable<Map.Entry<UUID, String>> {
    private String name;

    public InfoTask(String name) {
        this.name = name;
    }

    @Override
    public Map.Entry<UUID, String> call() throws Exception {
        UUID uuid = Util.uuid(name);
        if(uuid != null) {
            name = Util.name(uuid); // Proper capitalisation
        }
        return Maps.immutableEntry(uuid, name);
    }
}

class InfoResult implements FutureCallback<Map.Entry<UUID, String>> {
    private final CommandSender to;

    public InfoResult(CommandSender to) {
        this.to = to;
    }

    @Override
    public void onSuccess(Map.Entry<UUID, String> result) {
        NoirlandAutoPromote plugin = NoirlandAutoPromote.inst();
        GMHandler gmHandler = GMHandler.inst();
        PromoteConfig config = PromoteConfig.inst();

        UUID uuid = result.getKey();
        String name = result.getValue();

        if(uuid == null) {
            plugin.sendMessage(to, "That player has not played on this server.", true);
            return;
        }

        OfflinePlayer oPlayer = Util.player(uuid);

        if(!oPlayer.hasPlayedBefore() && !oPlayer.isOnline()) {
            plugin.sendMessage(to, "That player has not played on this server.", true);
            return;
        }

        if(oPlayer.isOnline()) {
            PromotionHandler.inst().checkForPromotion(oPlayer.getPlayer());
        }

        PlayerTimeData data = plugin.getTimeData(uuid);
        String group = gmHandler.getGroup(name);

        long neededMillis = config.getPlayTimeNeededMillis(group) - data.getPlayTime();

        plugin.sendMessage(to,"==== " + ChatColor.RED + "NoirPromote" + ChatColor.RESET + " ====", false);

        if(!config.getNoPromote(group)){
            String nextColor = gmHandler.getColor(name, true);
            String nextRank = config.getPromoteTo(group);
            plugin.sendMessage(to,"Time until " + nextColor + nextRank + ChatColor.RESET + ": " + Util.formatTime(neededMillis), false);
        }

        ArrayList<PlayerTimeData> set = plugin.getPlayerTimeData();
        int rank = set.indexOf(data) + 1;
        plugin.sendMessage(to,"Total Play Time: " + Util.formatTime(data.getTotalPlayTime()) + ChatColor.DARK_GRAY + " (#" + rank + ")", false);
    }

    @Override
    public void onFailure(Throwable error) {
        to.sendMessage(ChatColor.DARK_RED + "Internal error occurred!");
        NoirlandAutoPromote.debug().warning("Could not get promote info!", error);
    }
}
