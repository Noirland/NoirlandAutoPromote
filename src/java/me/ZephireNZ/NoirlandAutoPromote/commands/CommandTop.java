package me.ZephireNZ.NoirlandAutoPromote.commands;

import me.ZephireNZ.NoirlandAutoPromote.NoirlandAutoPromote;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CommandTop extends Command {

    public CommandTop(NoirlandAutoPromote plugin) {
        super(plugin);
    }

    public boolean run(CommandSender sender, String[] args) {
        if(sender.hasPermission("autopromote.check.top")) {
            switch(args.length) {
                case 1:
                    plugin.sendMessage(sender,"==== " + ChatColor.RED + "NoirPromote" + ChatColor.RESET + " ====");
                    String[] rankList = pmHandler.getRankedPlayerList(1);
                    for(String msg : rankList) {
                        plugin.sendMessage(sender, msg);
                    }
                    return true;
                case 2:
                    int page;
                    try {
                        page = Integer.parseInt(args[1]);
                    }catch(NumberFormatException e) {
                        plugin.sendMessage(sender,ChatColor.RED + "That page does not exist");
                        return false;
                    }
                    String[] pagedRankList = pmHandler.getRankedPlayerList(page);
                    if(pagedRankList.length > 0) {
                        plugin.sendMessage(sender,"==== " + ChatColor.RED + "NoirPromote" + ChatColor.RESET + " ====");
                        for(String msg : pagedRankList) {
                            plugin.sendMessage(sender, msg);
                        }
                        return true;
                    }else{
                        plugin.sendMessage(sender,ChatColor.RED + "That page does not exist");
                        return false;
                    }
                default:
                    printHelp(sender, "top");
                    return false;
            }
        }else{
            plugin.sendMessage(sender, ChatColor.RED + "You do not have access to that.");
            return false;
        }
    }
}
