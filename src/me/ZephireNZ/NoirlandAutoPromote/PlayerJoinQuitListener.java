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
	
	public PlayerJoinQuitListener(NoirlandAutoPromote plugin) {
		this.plugin = plugin;
		this.dbHandler = plugin.dbHandler;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		PlayerTimeObject pto = new PlayerTimeObject(player);
		pto.setJoinTime();
		plugin.playerTimeArray.add(pto);
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
