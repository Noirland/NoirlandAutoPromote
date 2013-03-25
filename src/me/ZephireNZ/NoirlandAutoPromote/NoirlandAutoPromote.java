package me.ZephireNZ.NoirlandAutoPromote;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class NoirlandAutoPromote extends JavaPlugin {
	
	ArrayList<PlayerTimeObject> playerTimeArray = new ArrayList<PlayerTimeObject>();
	DatabaseHandler dbHandler;
	GMHandler gmHandler;
	ConfigHandler confHandler;
	
	@Override
	public void onEnable(){
		dbHandler = new DatabaseHandler(this);
		gmHandler = new GMHandler(this);
		confHandler = new ConfigHandler(this);
		getServer().getPluginManager().registerEvents(new PlayerJoinQuitListener(this), this);
		
		for(Player player : getServer().getOnlinePlayers()) {
			PlayerTimeObject pto = new PlayerTimeObject(player); // Add online players to pto object after reload
			pto.setJoinTime();
			playerTimeArray.add(pto);
			
			if(checkForPromotion(player)) {
				promote(player, confHandler.getPromoteTo(gmHandler.getGroup(player))); // Check for promoteable player after reload
			}
		}
		
		new SaveTimesTask(this).runTaskTimer(this, confHandler.getSaveTimeSeconds() * 20L, confHandler.getSaveTimeSeconds() * 20L); // Save times to DB with time in config (in minutes)
		
		this.getCommand("autopromote").setExecutor(this);
		
		getServer().getPluginManager().registerEvents(new PlayerJoinQuitListener(this), this);
		getServer().getPluginManager().registerEvents(gmHandler, this);
		
		PluginDescriptionFile pdfFile = this.getDescription();
		getLogger().info(pdfFile.getName() + " version " + pdfFile.getVersion() + " started.");
	}
	
	@Override
	public void onDisable(){
		saveToDB(); // Save temporary playTimes to Database
		dbHandler.closeConnection();
		
		PluginDescriptionFile pdfFile = this.getDescription();
		getLogger().info(pdfFile.getName() + " version " + pdfFile.getVersion() + " stopped.");
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) { // Commands control for /autopromote
		try {
			if(sender instanceof Player) {
				if(args.length == 1 && sender.hasPermission("autopromote.check.others")) {
					if(getServer().getPlayerExact(args[0]) != null) {
						sender.sendMessage(ChatColor.RED + "[AutoPromote] " + ChatColor.RESET + "" + promoteInfo(getServer().getPlayerExact(args[0]), false));
					}else{
						sender.sendMessage(ChatColor.RED + "[AutoPromote] " + ChatColor.RESET + "That player has never played before.");
					}
				}else{
					if(sender.hasPermission("autopromote.check")){
						sender.sendMessage(ChatColor.RED + "[AutoPromote] " + ChatColor.RESET + "" + promoteInfo(((Player) sender).getPlayer(), true));
					}
				}
			}else{
				if(args.length >= 2){
					if(args[0].equals("promote")) {
						if(getServer().getPlayerExact(args[1]) != null) {
							Player player = getServer().getPlayerExact(args[1]);
							String rank = gmHandler.getGroup(player);
							String newRank;
							if(args.length == 3) {
								newRank = args[2];
							}else{
							newRank = confHandler.getPromoteTo(rank);
							}
							promote(player, newRank);
						}
						else{
							getLogger().warning("Player " + args[0] + " was promoted by the terminal, but has never played before.");
						}
					}
				}else if(args.length == 1){ 
					if(getServer().getPlayerExact(args[0]) != null) {
						getLogger().info("[AutoPromote] " + promoteInfo(getServer().getPlayerExact(args[0]), false));
					}else{
						getLogger().info("[AutoPromote] That player has never played before.");
					}
				}
			}
		}catch(ArrayIndexOutOfBoundsException e) {
			return false;
		}
		return true;
	}
	
	public void saveToDB() {
		for(PlayerTimeObject pto : playerTimeArray) {
			String player = pto.getPlayer().getName();
			pto.setQuitTime();
			dbHandler.setPlayTime(player, pto.getPlayTime() + dbHandler.getPlayTime(player));
			pto.setJoinTime();
			pto.resetQuitTime();
			
		}
	}
	
	public boolean checkForPromotion(Player player) { // Checks if player is eligible for promotion
		String currRank = gmHandler.getGroup(player);
		long playTime = dbHandler.getPlayTime(player.getName());
		if(confHandler.getNoPromote(currRank) == true) { // Don't promote ranks with "noPromote"
			return false;
		}
		if(playTime >= confHandler.getPlayTimeNeededMillis(currRank)) { // Check if player has played enough
			return true;
		}
	return false;
	}
	
	public void promote(Player player, String rank) {
		gmHandler.setGroup(player, rank);
		dbHandler.setPlayTime(player.getName(), 0);
		getServer().broadcastMessage(ChatColor.RED + "[AutoPromote] " + ChatColor.RESET + player.getName() + " has been promoted to " + gmHandler.getColor(player) + rank + ChatColor.RESET + "!");
	}
	
	public String promoteInfo(Player player, Boolean self) {
		
		long playTime = dbHandler.getPlayTime(player.getName());
		String rank = gmHandler.getGroup(player);
		for(PlayerTimeObject pto : playerTimeArray) {
			if(pto.getPlayer() == player) {
				playTime = playTime + (System.currentTimeMillis() - pto.getJoinTime());
			}
		}
		long neededMillis = confHandler.getPlayTimeNeededMillis(gmHandler.getGroup(player));
		long neededLeftHours = TimeUnit.HOURS.convert(neededMillis - playTime, TimeUnit.MILLISECONDS);
		
		if(self) {
			if(confHandler.getNoPromote(rank)) {
				return "You will not be promoted automatically.";
			}else{
				if(neededLeftHours >=1){
					return "You will be promoted after " + neededLeftHours + " more hours of play.";
				}else{
					long neededLeftMinutes = TimeUnit.MINUTES.convert(neededMillis - playTime, TimeUnit.MILLISECONDS);
					return "You will be promoted after " + neededLeftMinutes + " more minutes of play.";
				}
			}
		}else{
			if(confHandler.getNoPromote(rank)) {
				return player.getName() +  ChatColor.RESET + " will not be promoted automatically.";
			}else{
				if(neededLeftHours >=1){
					return player.getName() + ChatColor.RESET + " will be promoted after " + neededLeftHours + " more hours of play.";
				}else{
					long neededLeftMinutes = TimeUnit.MINUTES.convert(neededMillis - playTime, TimeUnit.MILLISECONDS);
					return player.getName() + ChatColor.RESET + " will be promoted after " + neededLeftMinutes + " more minutes of play.";
				}
			}
		}
	}
}

class SaveTimesTask extends BukkitRunnable {
	
	NoirlandAutoPromote plugin;
	
	public SaveTimesTask(NoirlandAutoPromote plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void run() {
		plugin.saveToDB(); // Save all players to DB on event
		
		for(Player player : plugin.getServer().getOnlinePlayers()) { // Check for promoteable player
			if(plugin.checkForPromotion(player)) {
				plugin.promote(player, plugin.confHandler.getPromoteTo(plugin.gmHandler.getGroup(player))); 
			}
		}
	}
}
