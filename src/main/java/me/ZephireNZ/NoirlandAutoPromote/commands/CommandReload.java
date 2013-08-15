package me.ZephireNZ.NoirlandAutoPromote.commands;

import me.ZephireNZ.NoirlandAutoPromote.NoirlandAutoPromote;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

class CommandReload extends Command {

    public CommandReload(NoirlandAutoPromote plugin) {
        super(plugin);
    }

    public boolean run(CommandSender sender, String[] args) {
        if(sender.hasPermission("autopromote.reload")) {
            plugin.reload();
            plugin.sendMessage(sender, " Reload successful.", true);
            return true;
        }else {
            plugin.sendMessage(sender, ChatColor.RED + "You do not have access to that.", false);
            return false;
        }

    }
}
