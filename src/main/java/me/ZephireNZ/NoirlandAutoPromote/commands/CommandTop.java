package me.ZephireNZ.NoirlandAutoPromote.commands;

import me.ZephireNZ.NoirlandAutoPromote.PlayerTimeData;
import me.ZephireNZ.NoirlandAutoPromote.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.NavigableMap;
import java.util.TreeMap;

class CommandTop extends Command {

    public boolean run(CommandSender sender, String[] args) {
        if (!sender.hasPermission("autopromote.check.top")) {
            plugin.sendMessage(sender, ChatColor.RED + "You do not have access to that.", false);
            return false;
        }

        int low;
        int up;
        TreeMap<Integer, PlayerTimeData> map = Util.getSortedMap(plugin.getPlayerTimeData());
        switch(args.length) {
            case 1:
                low = 1;
                up = 10;
                break;
            case 2:
                int page;
                try {
                    page = Integer.parseInt(args[1]);
                }catch(NumberFormatException e) {
                    plugin.sendMessage(sender, "That page does not exist", true);
                    return false;
                }

                low = (page-1) * 10 + 1;
                up = low + 9;
                break;
            default:
                printHelp(sender, "top");
                return false;
        }
        NavigableMap<Integer, PlayerTimeData> page = map.subMap(low, true, up, true);
        if(page.size() == 0) {
            plugin.sendMessage(sender, "That page does not exist", true);
            return false;
        }
        plugin.sendMessage(sender,"==== " + ChatColor.RED + "NoirPromote" + ChatColor.RESET + " ====", false);
        for(String msg : pmHandler.createRankedPlayerList(page)) {
            plugin.sendMessage(sender, msg, false);

        }
        return true;
    }
}
