package me.ZephireNZ.NoirlandAutoPromote;

import java.util.Iterator;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinQuitListener implements Listener {
	
	NoirlandAutoPromote plugin;
	DatabaseHandler dbHandler;
	ConfigHandler confHandler;
	GMHandler gmHandler;
	
	public PlayerJoinQuitListener(NoirlandAutoPromote plugin) {
		this.plugin = plugin;
		this.dbHandler = plugin.dbHandler;
		this.confHandler = plugin.confHandler;
		this.gmHandler = plugin.gmHandler;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		dbHandler.checkForPlayer(player.getName(), 0);
		PlayerTimeObject pto = new PlayerTimeObject(player);
		pto.setJoinTime();
		plugin.playerTimeArray.add(pto);
		if(plugin.checkForPromotion(player)) {
			plugin.promote(player, confHandler.getPromoteTo(gmHandler.getGroup(player)));
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		for(Iterator<PlayerTimeObject> it = plugin.playerTimeArray.iterator(); it.hasNext(); ) {
			PlayerTimeObject pto = it.next();
			if(pto.getPlayer() == player ) {
				pto.setQuitTime();
				dbHandler.setPlayTime(player.getName(), dbHandler.getPlayTime(player.getName()) + pto.getPlayTime());
				it.remove();
			}
		}
	}
}
