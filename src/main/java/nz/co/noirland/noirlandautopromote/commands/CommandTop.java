package nz.co.noirland.noirlandautopromote.commands;

import nz.co.noirland.noirlandautopromote.PlayerTimeData;
import nz.co.noirland.zephcore.Util;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

class CommandTop extends Command {

    public boolean run(CommandSender sender, String[] args) {
        if (!sender.hasPermission("autopromote.check.top")) {
            plugin.sendMessage(sender, ChatColor.RED + "You do not have access to that.", false);
            return false;
        }

        ArrayList<PlayerTimeData> list = plugin.getPlayerTimeData();
        int p;
        switch(args.length) {
            case 1:
                p = 1;
                break;
            case 2:
                try {
                    p = Integer.parseInt(args[1]);
                    break;
                }catch(NumberFormatException e) {
                    plugin.sendMessage(sender, "That page does not exist", true);
                    return false;
                }
            default:
                printHelp(sender, "top");
                return false;
        }
        int low = (p-1) * 10;
        int up = Math.min(low + 10, list.size());

        if(low >= list.size()) {
            plugin.sendMessage(sender, "That page does not exist", true);
            return false;
        }
        List<PlayerTimeData> page = list.subList(low, up);

        if(page.size() == 0) {
            plugin.sendMessage(sender, "That page does not exist", true);
            return false;
        }
        plugin.sendMessage(sender,"==== " + ChatColor.RED + "NoirPromote" + ChatColor.RESET + " ====", false);
        int totalPages = (int) Math.ceil((double) list.size() / 10);
        plugin.sendMessage(sender,"Page " + ChatColor.GOLD + p + ChatColor.RESET + " of " + ChatColor.GOLD + totalPages, false);

        int rank = low;
        ArrayList<String> ranks = new ArrayList<String>();
        for(PlayerTimeData data : page) {
            String pString = Util.name(data.getPlayer());
            String color = gmHandler.getGroupColor(gmHandler.getGroup(pString));
            if(color == null) {
                color = ChatColor.RESET.toString();
            }
            String msg = ++rank + ". " + color + pString + ChatColor.RESET + ": " + Util.formatTime(data.getTotalPlayTime());
            ranks.add(msg);
        }
        for(String msg : ranks) {
            plugin.sendMessage(sender, msg, false);

        }
        return true;
    }
}
