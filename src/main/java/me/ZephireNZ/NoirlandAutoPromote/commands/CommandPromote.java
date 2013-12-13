package me.ZephireNZ.NoirlandAutoPromote.commands;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class CommandPromote extends Command {

    public boolean run(CommandSender sender, String[] args) {
        OfflinePlayer oPlayer;
        Player player;
        if (!sender.hasPermission("autopromote.promote")) {
            plugin.sendMessage(sender, ChatColor.RED + "You do not have access to that.", false);
            return false;
        }
        switch(args.length) {
            case 1:
                printHelp(sender, "promote");
                return false;
            case 2:
                oPlayer = plugin.getServer().getOfflinePlayer(args[1]);
                player = oPlayer.getPlayer();
                if(player != null) {
                    String rank = gmHandler.getGroup(player);
                    if (config.getNoPromote(rank)) {
                        plugin.sendMessage(sender, player.getName() + " cannot be promoted further.", true);
                        return false;
                    }
                    String newRank = config.getPromoteTo(rank);
                    pmHandler.promote(sender, player, newRank);
                    return true;
                }else if (oPlayer.hasPlayedBefore()) {
                    plugin.sendMessage(sender, oPlayer.getName() + " must be online to be promoted.", true);
                    return false;
                } else {
                    plugin.sendMessage(sender, oPlayer.getName() + " has never player on this server.", true);
                    return false;
                }
            case 3:
                oPlayer = plugin.getServer().getOfflinePlayer(args[1]);
                player = oPlayer.getPlayer();
                if (player == null) {
                    printHelp(sender, "promote");
                    return false;
                }
                String newRank = args[2];
                pmHandler.promote(sender, player, newRank);
                return true;
            default:
                printHelp(sender, "promote");
                return false;
        }
    }
}
