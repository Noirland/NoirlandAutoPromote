package me.ZephireNZ.NoirlandAutoPromote.commands;

import me.ZephireNZ.NoirlandAutoPromote.NoirlandAutoPromote;
import me.ZephireNZ.NoirlandAutoPromote.PlayerTimeObject;
import org.bukkit.ChatColor;
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
                        plugin.sendMessage(sender, ChatColor.RED + "You do not have access to that.");
                        return false;
                    }
                }else{
                    plugin.sendMessage(sender, "Consoles cannot check their own play time.");
                    return false;
                }
            case 1:
                if(sender.hasPermission("autopromote.check.others")) {
                    Player player = plugin.getServer().getPlayerExact(args[0]);
                    if(player != null) {
                        promoteInfo(sender, player);
                        return true;
                    }else{
                        plugin.sendMessage(sender, ChatColor.RED + "[NoirPromote] " + ChatColor.RESET + "You can't check the times of offline or nonexistent players.");
                        return false;
                    }
                }else{
                    plugin.sendMessage(sender, ChatColor.RED + "You do not have access to that.");
                    return false;
                }
            default:
                printHelp(sender, "check");
                return false;
        }
    }

    void promoteInfo(CommandSender sender, Player player) {
        pmHandler.checkForPromotion(player);
        long playTime = dbHandler.getPlayTime(player.getName());
        long totalPlayTime = dbHandler.getTotalPlayTime(player.getName());
        for(PlayerTimeObject pto : plugin.playerTimeArray) {
            if(pto.getPlayer() == player) {
                playTime += (System.currentTimeMillis() - pto.getJoinTime());
                totalPlayTime += (System.currentTimeMillis() - pto.getJoinTime());
            }
        }
        long neededMillis = confHandler.getPlayTimeNeededMillis(gmHandler.getGroup(player)) - playTime;

        plugin.sendMessage(sender,"==== " + ChatColor.RED + "NoirPromote" + ChatColor.RESET + " ====");
        if(!confHandler.getNoPromote(gmHandler.getGroup(player))){
            String nextColor = gmHandler.getColor(player, true);
            String nextRank = confHandler.getPromoteTo(gmHandler.getGroup(player));
            plugin.sendMessage(sender,"Time until " + nextColor + nextRank + ChatColor.RESET + ": " + plugin.formatTime(neededMillis));
        }
        plugin.sendMessage(sender,"Total Play Time: " + plugin.formatTime(totalPlayTime));

        // Start new total rank testing


    }
}
