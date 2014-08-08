package nz.co.noirland.noirlandautopromote.commands;

import nz.co.noirland.noirlandautopromote.PlayerTimeData;
import nz.co.noirland.noirlandautopromote.callbacks.TopCallback;
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
        int numPages = (int) Math.ceil((double) list.size() / 10);

        if(low >= list.size()) {
            plugin.sendMessage(sender, "That page does not exist", true);
            return false;
        }
        List<PlayerTimeData> page = list.subList(low, up);

        if(page.size() == 0) {
            plugin.sendMessage(sender, "That page does not exist", true);
            return false;
        }

        new TopCallback(sender, new ArrayList<PlayerTimeData>(page), p, numPages, low);
        return true;
    }
}
