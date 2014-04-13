package nz.co.noirland.noirlandautopromote.commands;

import nz.co.noirland.noirlandautopromote.PlayerTimeData;
import nz.co.noirland.zephcore.Util;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.SortedSet;

class CommandFallback extends Command {

    public boolean run(CommandSender sender, String[] args) {

        switch(args.length) {
            case 0:
                if (!(sender instanceof Player)) {
                    plugin.sendMessage(sender, "Consoles cannot check their own play time.", false);
                    return false;
                }
                if (!sender.hasPermission("autopromote.check")) {
                    plugin.sendMessage(sender, ChatColor.RED + "You do not have access to that.", false);
                    return false;
                }
                Player pSender = (Player) sender;
                promoteInfo(sender, pSender);
                return true;
            case 1:
                if (!sender.hasPermission("autopromote.check.others")) {
                    plugin.sendMessage(sender, ChatColor.RED + "You do not have access to that.", false);
                    return false;
                }
                promoteInfo(sender, args[0]);
                return true;
            default:
                printHelp(sender, "check");
                printHelp(sender, "check.others");
                return false;
        }
    }

    void promoteInfo(CommandSender sender, String player) {
        OfflinePlayer oPlayer = Util.player(player);
        if(!oPlayer.hasPlayedBefore() && !oPlayer.isOnline()) {
            plugin.sendMessage(sender, "That player has not played on this server.", true);
            return;
        }
        Player p = oPlayer.getPlayer();
        PlayerTimeData data = plugin.getTimeData(oPlayer.getUniqueId());
        if(p != null) {
            pmHandler.checkForPromotion(p);
        }
        long neededMillis = config.getPlayTimeNeededMillis(gmHandler.getGroup(player)) - data.getPlayTime();

        plugin.sendMessage(sender,"==== " + ChatColor.RED + "NoirPromote" + ChatColor.RESET + " ====", false);
        if(!config.getNoPromote(gmHandler.getGroup(player))){
            String nextColor = gmHandler.getColor(player, true);
            String nextRank = config.getPromoteTo(gmHandler.getGroup(player));
            plugin.sendMessage(sender,"Time until " + nextColor + nextRank + ChatColor.RESET + ": " + Util.formatTime(neededMillis), false);
        }
        plugin.sortPlayerTimeData();
        SortedSet<PlayerTimeData> set = plugin.getPlayerTimeData();
        int rank = set.size() - set.tailSet(data).size() + 1;
        plugin.sendMessage(sender,"Total Play Time: " + Util.formatTime(data.getTotalPlayTime()) + ChatColor.DARK_GRAY + " (#" + rank + ")", false);
    }

    void promoteInfo(CommandSender sender, Player player) {
        promoteInfo(sender, player.getName());
    }
}
