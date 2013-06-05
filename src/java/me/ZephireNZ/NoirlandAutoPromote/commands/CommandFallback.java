package me.ZephireNZ.NoirlandAutoPromote.commands;

import me.ZephireNZ.NoirlandAutoPromote.NoirlandAutoPromote;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandFallback extends Command {

    public CommandFallback(NoirlandAutoPromote plugin) {
        super(plugin);
    }

    public boolean run(CommandSender sender, String[] args) {

        switch(args.length) {
            case 0:
                if(sender instanceof Player) {
                    if(sender.hasPermission("autopromote.check")) {
                        Player pSender = (Player) sender;
                        pmHandler.promoteInfo(sender, pSender);
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
                        pmHandler.promoteInfo(sender, player);
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
}
