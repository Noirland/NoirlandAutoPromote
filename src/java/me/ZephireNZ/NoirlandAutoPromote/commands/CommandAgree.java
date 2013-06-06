package me.ZephireNZ.NoirlandAutoPromote.commands;

import me.ZephireNZ.NoirlandAutoPromote.NoirlandAutoPromote;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandAgree extends Command {
    public CommandAgree(NoirlandAutoPromote plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            String currRank = gmHandler.getGroup(player);
            String rankDefault = confHandler.getDefault();
            if(currRank.equalsIgnoreCase(rankDefault)) {
                String newRank = confHandler.getPromoteTo(rankDefault);
                pmHandler.promote(player, newRank);
                return true;
            }else{
                plugin.sendMessage(sender, ChatColor.RED + "[NoirPromote] " + ChatColor.RESET + "You cannot be promoted, you are not " + confHandler.getDefault());
                return false;
            }
        }
        plugin.sendMessage(sender, "The console cannot be promoted");
        return false;
    }
}
