package me.ZephireNZ.NoirlandAutoPromote;

import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
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
				plugin.sendMessage(sender, "Consoles cannot check their own play time.");
			}
		}
		else if(args.length == 1) {
			if(args[0].toLowerCase().equals("reload") && sender.hasPermission("autopromote.reload")) {  //autopromote reload
				plugin.reload();
				plugin.sendMessage(sender, ChatColor.RED + "[NoirPromote]" + ChatColor.RESET + " Reload successful.");
			}else if(args[0].toLowerCase().equals("top") && sender.hasPermission("autopromote.check.others")) { // autopromote top
//				plugin.saveToDB();
				plugin.sendMessage(sender,"==== " + ChatColor.RED + "NoirPromote" + ChatColor.RESET + " ====");
				String[] rankList = getRankedPlayerList(1);
                for(String msg : rankList) {
                    plugin.sendMessage(sender, msg);
                }
			}else if(args[0].equals("help")) { // autopromote help
				plugin.sendMessage(sender,"==== " + ChatColor.RED + "NoirPromote" + ChatColor.RESET + " ====");
				if(sender.hasPermission("autopromote.check")) {
					plugin.sendMessage(sender, ChatColor.GRAY + "/autopromote " + ChatColor.RESET + "Show your promotion stats.");
				}
				if(sender.hasPermission("autopromote.check.others")) {
					plugin.sendMessage(sender, ChatColor.GRAY + "/autopromote [player] " + ChatColor.RESET + "Show anothers promotion stats.");
				}
				if(sender.hasPermission("autopromote.reload")) {
					plugin.sendMessage(sender, ChatColor.GRAY + "/autopromote reload " + ChatColor.RESET + "Reload the plugin.");
				}
				if(sender.hasPermission("autopromote.promote")) {
					plugin.sendMessage(sender, ChatColor.GRAY + "/autopromote promote [player] (rank) " + ChatColor.RESET + "Promote a player (optionally to specified rank).");
				}
				if(sender.hasPermission("autopromote.reset")) {
					plugin.sendMessage(sender, ChatColor.GRAY + "/autopromote reset [player] " + ChatColor.RESET + "Reset a player's play time (not total).");
				}
			
			}else{
				Player player = plugin.getServer().getPlayerExact(args[0]);
				if(player != null) {
					promoteInfo(sender, player);
					return true;
				}else{
					if(args[0].equals("promote") && sender.hasPermission("autopromote.promote")) {
						plugin.sendMessage(sender, ChatColor.RED + "[NoirPromote] " + ChatColor.RESET + "Promote must be followed by an online player.");
					}else if(args[0].equals("reset") && sender.hasPermission("autopromote.reset")) {
						plugin.sendMessage(sender, ChatColor.RED + "[NoirPromote] " + ChatColor.RESET + "Reset must be followed by a player.");
					}else{
						plugin.sendMessage(sender, ChatColor.RED + "[NoirPromote] " + ChatColor.RESET + "You can't check the times of offline or nonexistant players.");
					}
					return false;
				}
			}
		}
		else if(args.length == 2) {
            if(args[0].toLowerCase().equals("top") && sender.hasPermission("autopromote.check.others")) {
                int page;
                try {
                    page = Integer.parseInt(args[1]);
                }catch(NumberFormatException e) {
                    page = 1;
                }
                String[] rankList = getRankedPlayerList(page);
                if(rankList.length > 0) {
                    plugin.sendMessage(sender,"==== " + ChatColor.RED + "NoirPromote" + ChatColor.RESET + " ====");
                    for(String msg : rankList) {
                        plugin.sendMessage(sender, msg);
                    }
                }else{
                    plugin.sendMessage(sender,ChatColor.RED + "That page does not exist");
                }
            }
			OfflinePlayer oPlayer = plugin.getServer().getOfflinePlayer(args[1]);
			Player player = oPlayer.getPlayer();
			if(player != null && args[0].toLowerCase().equals("promote") && sender.hasPermission("autopromote.promote")) {
				String rank = gmHandler.getGroup(player);
				if(!confHandler.getNoPromote(rank)){
					String newRank = confHandler.getPromoteTo(rank);
					promote(player, newRank);
				}
				return true;
			}else if(args[0].toLowerCase().equals("reset") && sender.hasPermission("autopromote.reset")) {
				dbHandler.setPlayTime(oPlayer.getName(), 0);
				plugin.sendMessage(sender, ChatColor.RED + "[NoirPromote] " + ChatColor.RESET + oPlayer.getName() + "'s play time was reset.");
				return true;
			}else{
				if(args[0].equals("promote") && sender.hasPermission("autopromote.promote")) {
					plugin.sendMessage(sender, ChatColor.RED + "[NoirPromote] " + ChatColor.RESET + "Promote must be followed by an online player.");
				}else if(args[0].equals("reset") && sender.hasPermission("autopromote.reset")) {
					plugin.sendMessage(sender, ChatColor.RED + "[NoirPromote] " + ChatColor.RESET + "Reset must be followed by a player.");
				}
				return false;
			}
		}
		else if(args.length == 3) {
			Player player = plugin.getServer().getPlayerExact(args[1]);
			if(player != null && args[0].toLowerCase().equals("promote") && sender.hasPermission("autopromote.promote")) {
				String newRank = args[2];
				promote(player, newRank);
				return true;
			}else{
				if(args[0].equals("promote") && sender.hasPermission("autopromote.promote")) {
					plugin.sendMessage(sender, ChatColor.RED + "[NoirPromote] " + ChatColor.RESET + "Promote must be followed by an online player.");
				}
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
		plugin.getServer().broadcastMessage(ChatColor.RED + "[NoirPromote] " + ChatColor.RESET + player.getName() + " has been promoted to " + gmHandler.getColor(player, false) + rank + ChatColor.RESET + "!");
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
			return;
		} 
		else if(playTime >= confHandler.getPlayTimeNeededMillis(currRank)) { // Check if player has played enough
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
		
		plugin.sendMessage(sender,"==== " + ChatColor.RED + "NoirPromote" + ChatColor.RESET + " ====");
		if(!confHandler.getNoPromote(gmHandler.getGroup(player))){
			String nextColor = gmHandler.getColor(player, true);
			String nextRank = confHandler.getPromoteTo(gmHandler.getGroup(player));
			plugin.sendMessage(sender,"Time until " + nextColor + nextRank + ChatColor.RESET + ": " + plugin.formatTime(neededMillis));
		}
		plugin.sendMessage(sender,"Total Play Time: " + plugin.formatTime(totalPlayTime));
	}

    public String[] getRankedPlayerList(int startPage) {
        if(startPage <= 0) { return new String[0];} // Don't allow negative or zero pages
        Map<Integer, String> map = dbHandler.getRankedList(startPage);
        String[] returnArray = new String[map.size()];
        int offset = ((startPage-1)*10);
        for(int i = 1 + offset; i <=(map.size()+offset);i++) {
            OfflinePlayer oPlayer = plugin.getServer().getOfflinePlayer(map.get(i));
            Player player = oPlayer.getPlayer();
            String pString;
            String color;
            if(player != null) {
                color = gmHandler.getColor(player, false);
                pString = player.getName();
            }else{
                color = ChatColor.RESET.toString();
                pString = oPlayer.getName();
            }
            long totalPlayTime = dbHandler.getTotalPlayTime(pString);
            String msg = i + ". " + color + pString + ChatColor.RESET + ": " + plugin.formatTime(totalPlayTime);
            returnArray[i-offset-1] = msg;
        }
        return returnArray;
    }
	

}
