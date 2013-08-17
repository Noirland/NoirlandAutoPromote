package me.ZephireNZ.NoirlandAutoPromote.commands;

import me.ZephireNZ.NoirlandAutoPromote.NoirlandAutoPromote;
import me.ZephireNZ.NoirlandAutoPromote.tasks.VerifyFemaleTask;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class CommandFemale extends Command {

    public ArrayList<String> awaitingVerification = new ArrayList<String>();

    public CommandFemale(NoirlandAutoPromote plugin) {
        super(plugin);
    }

    public boolean run(CommandSender sender, String[] args) {

        if(!(sender instanceof Player)) {
            plugin.sendMessage(sender, "Console can't change its gender!", false);
            return true;
        }

        Player player = (Player) sender;
        if(awaitingVerification.contains(player.getName())) {
            awaitingVerification.remove(player.getName());
            plugin.gmHandler.toFemale(player);
        } else {
            if(confHandler.isFemale(gmHandler.getGroup(player))) {
                plugin.sendMessage(sender, "You're already the female version!", true);
                return true;
            }
            if(confHandler.getFemale(gmHandler.getGroup(player)) == null) {
                plugin.sendMessage(sender, "There is no female version of your rank!", true);
                return true;
            }
            awaitingVerification.add(player.getName());
            new VerifyFemaleTask(this, player.getName()).runTaskLater(plugin, 30 * 20L);
            plugin.sendMessage(sender, "Type " + ChatColor.ITALIC + "/autopromote female" + ChatColor.RESET + " again to change your rank. " + ChatColor.BOLD + "CANNOT BE REVERSED", true);
        }

        return true;
    }
}
