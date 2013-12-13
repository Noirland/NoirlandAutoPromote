package nz.co.noirland.noirlandautopromote.commands;

import nz.co.noirland.noirlandautopromote.PlayerTimeData;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.concurrent.TimeUnit;

class CommandReset extends Command {

    public boolean run(CommandSender sender, String[] args) {
        OfflinePlayer oPlayer;
        PlayerTimeData data;

        if (!sender.hasPermission("autopromote.reset")) {
            plugin.sendMessage(sender, ChatColor.RED + "You do not have access to that.", false);
            return false;
        }

        switch(args.length) {
            case 1:
                printHelp(sender, "reset");
                return false;
            case 2:
                oPlayer = plugin.getServer().getOfflinePlayer(args[1]);
                data = plugin.getTimeData(oPlayer.getName());
                data.setPlayTime(0);
                plugin.sendMessage(sender, oPlayer.getName() + "'s play time was reset.", true);
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
                data = plugin.getTimeData(oPlayer.getName());
                data.setPlayTime(TimeUnit.MILLISECONDS.convert(hours, TimeUnit.HOURS));
                plugin.sendMessage(sender, oPlayer.getName() + "'s play time was reset to " + hours + " hours.", true);
                return true;
        }
        return false;
    }
}
