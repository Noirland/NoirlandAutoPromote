package me.ZephireNZ.NoirlandAutoPromote;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PromotionHandler implements CommandExecutor{

	private NoirlandAutoPromote plugin;
	private GMHandler gmHandler;
	private DatabaseHandler dbHandler;
	private ConfigHandler confHandler;
	
	public PromotionHandler(NoirlandAutoPromote plugin) {
		this.plugin = plugin;
		this.gmHandler = plugin.gmHandler;
		this.dbHandler = plugin.dbHandler;
		this.confHandler = plugin.confHandler;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void promote(Player player, String rank) {
		gmHandler.setGroup(player, rank);
		dbHandler.setPlayTime(player.getName(), 0);
		plugin.getServer().broadcastMessage(ChatColor.RED + "[AutoPromote] " + ChatColor.RESET + player.getName() + " has been promoted to " + gmHandler.getColor(player) + rank + ChatColor.RESET + "!");
	}
	
	public void checkForPromotion(Player player) { // Checks if player is eligible for promotion
		String currRank = gmHandler.getGroup(player);
		long playTime = dbHandler.getPlayTime(player.getName());
		if(confHandler.getNoPromote(currRank) == true) { // Don't promote ranks with "noPromote"
		}
		if(playTime >= confHandler.getPlayTimeNeededMillis(currRank)) { // Check if player has played enough
			promote(player, confHandler.getPromoteTo(gmHandler.getGroup(player)));
		}
	}
}
