package me.ZephireNZ.NoirlandAutoPromote.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandAgree extends Command {
    public CommandAgree() {
        super();
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            String currRank = gmHandler.getGroup(player);
            String rankDefault = config.getDefault();
            if(currRank.equalsIgnoreCase(rankDefault)) {
                String newRank = config.getPromoteTo(rankDefault);
                pmHandler.promote(sender, player, newRank);
                return true;
            }else{
                plugin.sendMessage(sender, "You cannot be promoted, you are not " + config.getDefault(), true);
                return false;
            }
        }
        plugin.sendMessage(sender, "The console cannot be promoted.", false);
        return false;
    }
}
