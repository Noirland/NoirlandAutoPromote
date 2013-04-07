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
		if(args.length == 0) {
			if(sender instanceof Player) {
					Player pSender = (Player) sender;
					promoteInfo(sender, pSender);
					return true;
			}else{
				return false; //TODO: Permission sensitive help
			}
		}
		else if(args.length == 1) {
			if(args[0].toLowerCase().equals("reload") && sender.hasPermission("autopromote.reload")) {
				plugin.reload();
				if(sender instanceof Player) {
					plugin.sendMessage(sender, ChatColor.RED + "[NoirPromote]" + ChatColor.RESET + " Reload successful.");
					return true;
				}
			}else{
				Player player = plugin.getServer().getPlayerExact(args[0]);
				if(player != null) {
					promoteInfo(sender, player);
					return true;
				}else{
					return false; //TODO: Permission sensitive help
				}
			}
		}
		else if(args.length == 2) {
			Player player = plugin.getServer().getPlayerExact(args[1]);
			if(player !=null) {
				if(args[0].toLowerCase().equals("promote") && sender.hasPermission("autopromote.promote")) {
					String rank = gmHandler.getGroup(player);
					String newRank = confHandler.getPromoteTo(rank);
					promote(player, newRank);
					return true;
				}
				else if(args[0].toLowerCase().equals("reset") && sender.hasPermission("autopromote.reset")) {
					dbHandler.setPlayTime(player.getName(), 0);
					plugin.sendMessage(sender, ChatColor.RED + "[NoirPromote] " + ChatColor.RESET + player.getName() + "'s play time was reset.");
					return true;
				}else{
					return false; //TODO: Permission sensitive help
				}
			}
		}
		else if(args.length == 3) {
			Player player = plugin.getServer().getPlayerExact(args[1]);
			if(player !=null) {
				if(args[0].toLowerCase().equals("promote") && sender.hasPermission("autopromote.promote")) {
					String newRank = args[2];
					promote(player, newRank);
					return true;
				}else{
					return false; //TODO: Permission sensitive help
				}
			}else{
				return false;
			}
		}else{
			return false;
		}
		return false;
	}
	
	public void promote(Player player, String rank) {
		gmHandler.setGroup(player, rank);
		dbHandler.setPlayTime(player.getName(), 0);
		plugin.getServer().broadcastMessage(ChatColor.RED + "[NoirPromote] " + ChatColor.RESET + player.getName() + " has been promoted to " + gmHandler.getColor(player) + rank + ChatColor.RESET + "!");
	}
	
	public void checkForPromotion(Player player) { // Checks if player is eligible for promotion
		String currRank = gmHandler.getGroup(player);
		long playTime = dbHandler.getPlayTime(player.getName());
		for(PlayerTimeObject pto : plugin.playerTimeArray) {
			if(pto.getPlayer() == player) {
				playTime = playTime + (System.currentTimeMillis() - pto.getJoinTime());
			}
		}
		if(confHandler.getNoPromote(currRank) == true) { // Don't promote ranks with "noPromote"
		}
		if(playTime >= confHandler.getPlayTimeNeededMillis(currRank)) { // Check if player has played enough
			promote(player, confHandler.getPromoteTo(gmHandler.getGroup(player)));
		}
	}
	
	public void promoteInfo(CommandSender sender, Player player) {
		checkForPromotion(player);
		long playTime = dbHandler.getPlayTime(player.getName());
		long totalPlayTime = dbHandler.getTotalPlayTime(player.getName());
		for(PlayerTimeObject pto : plugin.playerTimeArray) {
			if(pto.getPlayer() == player) {
				playTime += (System.currentTimeMillis() - pto.getJoinTime());
				totalPlayTime += (System.currentTimeMillis() - pto.getJoinTime());
			}
		}
		long neededMillis = confHandler.getPlayTimeNeededMillis(gmHandler.getGroup(player)) - playTime;
		String pColor = gmHandler.getColor(player);
		String nextRank = confHandler.getPromoteTo(gmHandler.getGroup(player));
		
		plugin.sendMessage(sender,"==== " + ChatColor.RED + "NoirPromote" + ChatColor.RESET + " ====");
		plugin.sendMessage(sender,"Time until " + pColor + nextRank + ChatColor.RESET + ": " + plugin.formatTime(neededMillis));
		plugin.sendMessage(sender,"Total Play Time: " + plugin.formatTime(totalPlayTime));
	}
	

}
