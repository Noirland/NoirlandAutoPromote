package me.ZephireNZ.NoirlandAutoPromote.commands;

import me.ZephireNZ.NoirlandAutoPromote.NoirlandAutoPromote;
import me.ZephireNZ.NoirlandAutoPromote.PlayerTimeObject;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class CommandFallback extends Command {

    public CommandFallback(NoirlandAutoPromote plugin) {
        super(plugin);
    }

    public boolean run(CommandSender sender, String[] args) {

        switch(args.length) {
            case 0:
                if(sender instanceof Player) {
                    if(sender.hasPermission("autopromote.check")) {
                        Player pSender = (Player) sender;
                        promoteInfo(sender, pSender);
                        return true;
                    }else{
                        plugin.sendMessage(sender, ChatColor.RED + "You do not have access to that.", false);
                        return false;
                    }
                }else{
                    plugin.sendMessage(sender, "Consoles cannot check their own play time.", false);
                    return false;
                }
            case 1:
                if(sender.hasPermission("autopromote.check.others")) {
                        promoteInfo(sender, args[0]);
                        return true;
                }else{
                    plugin.sendMessage(sender, ChatColor.RED + "You do not have access to that.", false);
                    return false;
                }
            default:
                printHelp(sender, "check");
                printHelp(sender, "check.others");
                return false;
        }
    }

    void promoteInfo(CommandSender sender, String player) {
        Player p = plugin.getServer().getPlayer(player);
        OfflinePlayer oPlayer = plugin.getServer().getOfflinePlayer(player);
        if(!oPlayer.hasPlayedBefore()) {
            plugin.sendMessage(sender, "That player has not played on this server.", true);
            return;
        }
        long playTime = dbHandler.getPlayTime(player);
        long totalPlayTime = dbHandler.getTotalPlayTime(player);
        if(p != null) {
            pmHandler.checkForPromotion(p);
            for(PlayerTimeObject pto : plugin.playerTimeArray) {
                if(pto.getPlayer() == p) {
                    playTime += (System.currentTimeMillis() - pto.getJoinTime());
                    totalPlayTime += (System.currentTimeMillis() - pto.getJoinTime());
                }
            }
        }
        long neededMillis = confHandler.getPlayTimeNeededMillis(gmHandler.getGroup(player)) - playTime;
        plugin.sendMessage(sender,"==== " + ChatColor.RED + "NoirPromote" + ChatColor.RESET + " ====", false);
        if(!confHandler.getNoPromote(gmHandler.getGroup(player))){
            String nextColor = gmHandler.getColor(player, true);
            String nextRank = confHandler.getPromoteTo(gmHandler.getGroup(player));
            plugin.sendMessage(sender,"Time until " + nextColor + nextRank + ChatColor.RESET + ": " + plugin.formatTime(neededMillis), false);
        }
        String rankString = "";
        if(dbHandler.getPlayerRank(player) != 0) {
            rankString = ChatColor.DARK_GRAY + " (#" + dbHandler.getPlayerRank(player) + ")";
        }
        plugin.sendMessage(sender,"Total Play Time: " + plugin.formatTime(totalPlayTime) + rankString, false);
    }

    void promoteInfo(CommandSender sender, Player player) {
        promoteInfo(sender, player.getName());
    }
}
