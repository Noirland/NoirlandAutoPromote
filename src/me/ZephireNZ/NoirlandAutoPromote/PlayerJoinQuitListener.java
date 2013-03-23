package me.ZephireNZ.NoirlandAutoPromote;

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
		plugin.getLogger().info(player.getName() + " Join Time: " + pto.getJoinTime());
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		for(PlayerTimeObject pto : plugin.playerTimeArray) {
			if(pto.getPlayer() == player ) {
				pto.setQuitTime();
				plugin.getLogger().info(player.getName() + " Quit Time: " + pto.getQuitTime());
				plugin.getLogger().info(player.getName() + " Play time:" + pto.getPlayTime());
				dbHandler.setPlayTime(player.getName(), dbHandler.getPlayTime(player.getName()) + pto.getPlayTime());
				plugin.getLogger().info(player.getName() + " Total Play time: " + dbHandler.getPlayTime(player.getName()));
			}
		}
	}
}
