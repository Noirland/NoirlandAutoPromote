package nz.co.noirland.noirlandautopromote;

import nz.co.noirland.noirlandautopromote.config.PluginConfig;
import nz.co.noirland.noirlandautopromote.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Map;
import java.util.NavigableMap;

public class PromotionHandler {

    private final NoirlandAutoPromote plugin = NoirlandAutoPromote.inst();
    private final GMHandler gmHandler = GMHandler.inst();
    private final PluginConfig config = PluginConfig.inst();
    private static PromotionHandler inst;

    public static PromotionHandler inst() {
        if(inst == null) {
            inst = new PromotionHandler();
        }
        return inst;
    }

    private PromotionHandler() {
    }

    public void promote(CommandSender sender, Player player, String rank) {
        PlayerTimeData data = plugin.getTimeData(player.getName());
        if(sender == null) {
            sender = plugin.getServer().getConsoleSender();
        }
        if(!gmHandler.setGroup(player, rank)) {
            plugin.sendMessage(sender, ChatColor.RED + "That rank does not exist!", false);
        }else{
        data.setPlayTime(0);
        plugin.getServer().broadcastMessage(ChatColor.RED + "[NoirPromote] " + ChatColor.RESET + player.getName() + " has been promoted to " + gmHandler.getColor(player, false) + rank + ChatColor.RESET + "!");
        }
    }

    public void checkForPromotion(Player player) { // Checks if player is eligible for promotion
        PlayerTimeData data = plugin.getTimeData(player.getName());
        String currRank = gmHandler.getGroup(player);
        if(!config.getNoPromote(currRank) && data.getPlayTime() >= config.getPlayTimeNeededMillis(currRank)) { // Only promote those without noPromote
            promote(null, player, config.getPromoteTo(gmHandler.getGroup(player)));
        }
    }

    public ArrayList<String> createRankedPlayerList(NavigableMap<Integer, PlayerTimeData> page) {
        ArrayList<String> ret = new ArrayList<String>();
        for(Map.Entry<Integer, PlayerTimeData> entry : page.entrySet()) {
            PlayerTimeData data = entry.getValue();
            OfflinePlayer oPlayer = plugin.getServer().getOfflinePlayer(data.getPlayer());
            String pString = oPlayer.getName();
            String color = gmHandler.getGroupColor(gmHandler.getGroup(pString));
            if(color == null) {
                color = ChatColor.RESET.toString();
            }
            String msg = entry.getKey() + ". " + color + pString + ChatColor.RESET + ": " + Util.formatTime(data.getTotalPlayTime());
            ret.add(msg);
        }
        return ret;
    }
}