package me.ZephireNZ.NoirlandAutoPromote;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinQuitListener implements Listener {
	
	NoirlandAutoPromote plugin;
	
	public PlayerJoinQuitListener(NoirlandAutoPromote plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		PlayerTimeObject ppt = new PlayerTimeObject(player);
		ppt.setJoinTime();
		plugin.PlayerPlayTimerArray.add(ppt);
		plugin.getLogger().info(player.getName() + " Join Time: " + ppt.getJoinTime());
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		for(PlayerTimeObject ppt : plugin.PlayerPlayTimerArray) {
			if(ppt.getPlayer() == player ) {
				ppt.setQuitTime();
				plugin.getLogger().info(player.getName() + " Quit Time: " + ppt.getQuitTime());
				plugin.getLogger().info(player.getName() + " Total Play time:" + ppt.getPlayTime());
			}
		}
	}
}
