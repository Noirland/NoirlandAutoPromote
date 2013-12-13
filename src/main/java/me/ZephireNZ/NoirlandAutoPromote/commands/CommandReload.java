package me.ZephireNZ.NoirlandAutoPromote.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

class CommandReload extends Command {

    public boolean run(CommandSender sender, String[] args) {
        if (!sender.hasPermission("autopromote.reload")) {
            plugin.sendMessage(sender, ChatColor.RED + "You do not have access to that.", false);
            return false;
        }
        plugin.reload();
        plugin.sendMessage(sender, " Reload successful.", true);
        return true;

    }
}
