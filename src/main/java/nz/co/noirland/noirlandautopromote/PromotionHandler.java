package nz.co.noirland.noirlandautopromote;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PromotionHandler {

    private final NoirlandAutoPromote plugin = NoirlandAutoPromote.inst();
    private final GMHandler gmHandler = GMHandler.inst();
    private final PromoteConfig config = PromoteConfig.inst();
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
        PlayerTimeData data = plugin.getTimeData(player.getUniqueId());
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
        PlayerTimeData data = plugin.getTimeData(player.getUniqueId());
        String currRank = gmHandler.getGroup(player);
        if(!config.getNoPromote(currRank) && data.getPlayTime() >= config.getPlayTimeNeededMillis(currRank)) { // Only promote those without noPromote
            promote(null, player, config.getPromoteTo(gmHandler.getGroup(player)));
        }
    }
}