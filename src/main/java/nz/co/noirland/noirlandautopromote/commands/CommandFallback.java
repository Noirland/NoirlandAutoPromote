package nz.co.noirland.noirlandautopromote.commands;

import nz.co.noirland.noirlandautopromote.callbacks.InfoCallback;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class CommandFallback extends Command {

    public boolean run(CommandSender sender, String[] args) {
        String player;

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
                player = sender.getName();
                break;
            case 1:
                if (!sender.hasPermission("autopromote.check.others")) {
                    plugin.sendMessage(sender, ChatColor.RED + "You do not have access to that.", false);
                    return false;
                }
                player = args[0];
                break;
            default:
                printHelp(sender, "check");
                printHelp(sender, "check.others");
                return false;
        }

        new InfoCallback(sender, player);
        return true;
    }
}
