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
		confHandler = new ConfigHandler(this);
		dbHandler = new DatabaseHandler(this);
		gmHandler = new GMHandler(this);
		getServer().getPluginManager().registerEvents(new PlayerJoinQuitListener(this), this);
		
		for(Player player : getServer().getOnlinePlayers()) {
			debug("onEnable Player: " + player.getName());
			PlayerTimeObject pto = new PlayerTimeObject(player); // Add online players to pto object after reload
			pto.setJoinTime();
			debug("onEnable JoinTime: " + pto.getJoinTime());
			playerTimeArray.add(pto);
			
			if(checkForPromotion(player)) {
				debug("checkForPromotion Player " + player.getName() + "being promoted");
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
			debug("onCommand args: " + args.toString());
			if(sender instanceof Player) {
				debug("onCommand Player: " + sender.getName());
				if(args.length == 1 && sender.hasPermission("autopromote.check.others")) {
					debug("onCommand argsLen 1 has perm");
					if(getServer().getPlayerExact(args[0]) != null) {
						debug("onCommand Player " + args[0] + " exists");
						sender.sendMessage(ChatColor.RED + "[AutoPromote] " + ChatColor.RESET + "" + promoteInfo(getServer().getPlayerExact(args[0]), false));
					}else{
						sender.sendMessage(ChatColor.RED + "[AutoPromote] " + ChatColor.RESET + "That player has never played before.");
					}
				}else{
					debug("onCommand argLen not 1 or no perm");
					if(sender.hasPermission("autopromote.check")){
						debug("onCommand has perm");
						sender.sendMessage(ChatColor.RED + "[AutoPromote] " + ChatColor.RESET + "" + promoteInfo(((Player) sender).getPlayer(), true));
					}
				}
			}else{
				debug("onCommand Console");
				if(args.length >= 2){
					debug("onCommand args >=2");
					if(args[0].equals("promote")) {
						debug("onCommand arg promote");
						if(getServer().getPlayerExact(args[1]) != null) {
							debug("onCommand player args[1] not exists");
							Player player = getServer().getPlayerExact(args[1]);
							String rank = gmHandler.getGroup(player);
							String newRank;
							debug("onCommand player: " + player.getName());
							debug("onCommand rank: " + rank);
							if(args.length == 3) {
								newRank = args[2];
								debug("onCommand args len 3, new Rank: " + newRank);
							}else{
							newRank = confHandler.getPromoteTo(rank);
							debug("onCommand newRank: " + newRank);
							}
							promote(player, newRank);
						}
						else{
							getLogger().warning("[AutoPromote] Player " + args[1] + " was promoted to by the terminal, but has never played before.");
						}
					}
				}else if(args.length == 1){ 
					debug("onCommand argsLen 1");
					if(args[0] == "reload") {
						reload();
					}else{
						if(getServer().getPlayerExact(args[0]) != null) {
							debug("onCommand player " + args[0] + "exists");
							getLogger().info("[AutoPromote] " + promoteInfo(getServer().getPlayerExact(args[0]), false));
						}else{
							getLogger().info("[AutoPromote] That player has never played before.");
						}
					}
				}
			}
		}catch(ArrayIndexOutOfBoundsException e) {
			debug("onCommand ArrayIndexOutOfBoundsException: " + e.getMessage());
			debug(e.getStackTrace().toString());
			return false;
		}
		return true;
	}
	
	public void saveToDB() {
		debug("saveToDB");
		for(PlayerTimeObject pto : playerTimeArray) {
			String player = pto.getPlayer().getName();
			pto.setQuitTime();
			debug("saveToDB player: " + player);
			debug("saveToDB quitTime: " + pto.getQuitTime());
			debug("saveToDB oldPlayTime: " + dbHandler.getPlayTime(player));
			dbHandler.setPlayTime(player, pto.getPlayTime() + dbHandler.getPlayTime(player));
			debug("saveToDB newPlayTime: " + dbHandler.getPlayTime(player));
			pto.setJoinTime();
			debug("saveToDB joinTime: " + pto.getJoinTime());
			pto.resetQuitTime();
		}
	}
	
	public void reload() {
		saveToDB();
		confHandler.loadConfig();
		dbHandler.closeConnection();
		dbHandler.SQLConnect();
		for(Player player : getServer().getOnlinePlayers()) {
			if(checkForPromotion(player)) {
				debug("reload Player " + player.getName() + " being promoted");
				promote(player, confHandler.getPromoteTo(gmHandler.getGroup(player))); // Check for promoteable player after reload
			}
		}
	}
	
	public boolean checkForPromotion(Player player) { // Checks if player is eligible for promotion
		debug("checkForPromotion player: " + player.getName());
		String currRank = gmHandler.getGroup(player);
		debug("checkForPromotion currRank: " + currRank);
		long playTime = dbHandler.getPlayTime(player.getName());
		debug("checkForPromotion playTime: " + playTime);
		debug("checkForPromotion noPromote: " + confHandler.getNoPromote(currRank));
		if(confHandler.getNoPromote(currRank) == true) { // Don't promote ranks with "noPromote"
			debug("checkForPromotion noPromote");
			return false;
		}
		debug("checkForPromotion playTime: " + playTime + ", Needed: " + confHandler.getPlayTimeNeededMillis(currRank));
		if(playTime >= confHandler.getPlayTimeNeededMillis(currRank)) { // Check if player has played enough
			debug("checkForPromotion playTime >= Needed");
			return true;
		}
		return false;
	}
	
	public void debug(String message) {
		if(confHandler.getDebug()) {
			getLogger().info("[DEBUG] " + message);
		}
	}
	
	public void promote(Player player, String rank) {
		debug("promote player: " + player.getName() + ", rank: " + rank);
		gmHandler.setGroup(player, rank);
		dbHandler.setPlayTime(player.getName(), 0);
		getServer().broadcastMessage(ChatColor.RED + "[AutoPromote] " + ChatColor.RESET + player.getName() + " has been promoted to " + gmHandler.getColor(player) + rank + ChatColor.RESET + "!");
	}
	
	public String promoteInfo(Player player, Boolean self) {
		
		long playTime = dbHandler.getPlayTime(player.getName());
		String rank = gmHandler.getGroup(player);
		debug("promoteInfo player: " + player.getName());
		debug("promoteInfo playTime: " + playTime);
		debug("promoteInfo rank: " + rank);
		for(PlayerTimeObject pto : playerTimeArray) {
			if(pto.getPlayer() == player) {
				playTime = playTime + (System.currentTimeMillis() - pto.getJoinTime());
				debug("promoteInfo for pto newPlayTime: " + playTime);
			}
		}
		long neededMillis = confHandler.getPlayTimeNeededMillis(gmHandler.getGroup(player));
		debug("promoteInfo neededMillis: " + neededMillis);
		long neededLeftHours = TimeUnit.HOURS.convert(neededMillis - playTime, TimeUnit.MILLISECONDS);
		debug("promoteInfo neededLeftHours: " + neededLeftHours);
		
		debug("promoteInfo self: " + self);
		if(self) {
			debug("promoteInfo self");
			debug("promoteInfo noPromote: " + confHandler.getNoPromote(rank));
			if(confHandler.getNoPromote(rank)) {
				return "You will not be promoted automatically.";
			}else{
				if(neededLeftHours >=1){
					debug("promoteInfo neededLeftHours >= 1: " + neededLeftHours);
					return "You will be promoted after " + neededLeftHours + " more hours of play.";
				}else{
					long neededLeftMinutes = TimeUnit.MINUTES.convert(neededMillis - playTime, TimeUnit.MILLISECONDS);
					debug("promoteInfo neededLeftMinutes: " + neededLeftMinutes);
					return "You will be promoted after " + neededLeftMinutes + " more minutes of play.";
				}
			}
		}else{
			debug("promoteInfo !self");
			debug("promoteInfo noPromote: " + confHandler.getNoPromote(rank));
			if(confHandler.getNoPromote(rank)) {
				return player.getName() +  ChatColor.RESET + " will not be promoted automatically.";
			}else{
				if(neededLeftHours >=1){
					debug("promoteInfo neededLeftHours >= 1: " + neededLeftHours);
					return player.getName() + ChatColor.RESET + " will be promoted after " + neededLeftHours + " more hours of play.";
				}else{
					long neededLeftMinutes = TimeUnit.MINUTES.convert(neededMillis - playTime, TimeUnit.MILLISECONDS);
					debug("promoteInfo neededLeftMinutes: " + neededLeftMinutes);
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
		plugin.debug("SaveTimeTask started");
	}
	
	@Override
	public void run() {
		plugin.debug("SaveTimeTask run saveToDB");
		plugin.saveToDB(); // Save all players to DB on event
		
		for(Player player : plugin.getServer().getOnlinePlayers()) { // Check for promoteable player
			plugin.debug("SaveTimeTask run player: " + player.getName());
			if(plugin.checkForPromotion(player)) {
				plugin.debug("SaveTimeTask run checkForPromotion true");
				plugin.promote(player, plugin.confHandler.getPromoteTo(plugin.gmHandler.getGroup(player))); 
			}
		}
	}
}
