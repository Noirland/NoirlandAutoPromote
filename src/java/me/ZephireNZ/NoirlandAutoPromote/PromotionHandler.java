package me.ZephireNZ.NoirlandAutoPromote;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class PromotionHandler {

    private final NoirlandAutoPromote plugin;
    private final GMHandler gmHandler;
    private final DatabaseHandler dbHandler;
    private final ConfigHandler confHandler;

    public PromotionHandler(NoirlandAutoPromote plugin) {
        this.plugin = plugin;
        this.gmHandler = plugin.gmHandler;
        this.dbHandler = plugin.dbHandler;
        this.confHandler = plugin.confHandler;
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
        if(!confHandler.getNoPromote(currRank) && playTime >= confHandler.getPlayTimeNeededMillis(currRank)) { // Only promote those without noPromote
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