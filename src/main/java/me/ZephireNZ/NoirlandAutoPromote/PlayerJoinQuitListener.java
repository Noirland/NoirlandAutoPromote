package me.ZephireNZ.NoirlandAutoPromote;

import me.ZephireNZ.NoirlandAutoPromote.database.Database;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

class PlayerJoinQuitListener implements Listener {
	
	private final NoirlandAutoPromote plugin = NoirlandAutoPromote.inst();
    private final PromotionHandler pmHandler = PromotionHandler.inst();

	public PlayerJoinQuitListener() {
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
        PlayerTimeData data = plugin.getTimeData(player.getName());
        data.joined();
		pmHandler.checkForPromotion(player);
    }
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
        PlayerTimeData data = plugin.getTimeData(player.getName());
        data.left();
        Database.inst().updatePlayerTimes(data.getPlayer(), data.getPlayTime(), data.getTotalPlayTime(), true);
        data.setChanged(false);
	}
}
