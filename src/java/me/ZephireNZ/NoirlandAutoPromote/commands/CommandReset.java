package me.ZephireNZ.NoirlandAutoPromote.commands;

import me.ZephireNZ.NoirlandAutoPromote.NoirlandAutoPromote;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.concurrent.TimeUnit;

class CommandReset extends Command {

    public CommandReset(NoirlandAutoPromote plugin) {
        super(plugin);
    }

    public boolean run(CommandSender sender, String[] args) {
        OfflinePlayer oPlayer;
        if(sender.hasPermission("autopromote.reset")) {
            switch(args.length) {
                case 1:
                    printHelp(sender, "reset");
                    return false;
                case 2:
                        oPlayer = plugin.getServer().getOfflinePlayer(args[1]);
                        dbHandler.setPlayTime(oPlayer.getName(), 0);
                        plugin.sendMessage(sender, ChatColor.RED + "[NoirPromote] " + ChatColor.RESET + oPlayer.getName() + "'s play time was reset.");
                        return true;
                case 3:
                    int hours;
                    try {
                        hours = Integer.parseInt(args[2]);
                    }catch(NumberFormatException e) {
                        printHelp(sender, "reset");
                        return false;
                    }
                        oPlayer = plugin.getServer().getOfflinePlayer(args[1]);
                        dbHandler.setPlayTime(oPlayer.getName(), TimeUnit.MILLISECONDS.convert(hours, TimeUnit.HOURS));
                        plugin.sendMessage(sender, ChatColor.RED + "[NoirPromote] " + ChatColor.RESET + oPlayer.getName() + "'s play time was reset to " + hours + " hours.");
                        return true;
            }
        }else{
            plugin.sendMessage(sender, ChatColor.RED + "You do not have access to that.");
            return false;
        }
        return false;
    }
}
