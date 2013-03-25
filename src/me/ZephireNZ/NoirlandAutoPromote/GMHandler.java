package me.ZephireNZ.NoirlandAutoPromote;

import org.anjocaido.groupmanager.GroupManager;
import org.anjocaido.groupmanager.dataholder.OverloadedWorldHolder;
import org.anjocaido.groupmanager.permissions.AnjoPermissionsHandler;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class GMHandler implements Listener {
	private GroupManager gm;
	private NoirlandAutoPromote plugin;
	
	public GMHandler(NoirlandAutoPromote plugin) {
		final PluginManager pm = plugin.getServer().getPluginManager();
		final Plugin GMplugin = pm.getPlugin("GroupManager");
		this.plugin = plugin;
 
		if (GMplugin != null && GMplugin.isEnabled()) {
			gm = (GroupManager)GMplugin;
 
		}
		
		plugin.debug("GMHandler started");
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPluginDisable(PluginDisableEvent event) {
		plugin.debug("GMHandler onPluginDisable plugin: " + event.getPlugin().getName());
		if (gm != null) {
				gm = null;
		}
	}
	
	public String getGroup(final Player player) {
		plugin.debug("GMHandler getGroup player: " + player.getName());
		final AnjoPermissionsHandler handler = gm.getWorldsHolder().getWorldPermissions(player);
		if (handler == null) {
			return null;
		}
		plugin.debug("GMHandler getGroup group: " + handler.getGroup(player.getName()));
		return handler.getGroup(player.getName());
	}
	
	public String getPrefix(final Player player) {
		plugin.debug("GMHandler getPrefix player: " + player.getName());
		final AnjoPermissionsHandler handler = gm.getWorldsHolder().getWorldPermissions(player);
		if (handler == null) {
			return null;
		}
		return handler.getGroupPrefix(getGroup(player));
	}
	
	public String getColor(final Player player) {
		plugin.debug("GMHandler getColor player: " + player.getName());
		final AnjoPermissionsHandler handler = gm.getWorldsHolder().getWorldPermissions(player);
		if (handler == null) {
			return null;
		}
		return ChatColor.translateAlternateColorCodes("&".charAt(0), handler.getGroupPrefix(getGroup(player)));
	}
 
	public boolean setGroup(final Player player, final String group) {
		plugin.debug("GMHandler setGroup player: " + player.getName() + ", group: " + group);
		final OverloadedWorldHolder handler = gm.getWorldsHolder().getWorldData(player);
		if (handler == null) {
			return false;
		}
		handler.getUser(player.getName()).setGroup(handler.getGroup(group));
		return true;
	}
}
