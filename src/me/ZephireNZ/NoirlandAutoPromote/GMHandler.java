package me.ZephireNZ.NoirlandAutoPromote;

import org.anjocaido.groupmanager.GroupManager;
import org.anjocaido.groupmanager.dataholder.OverloadedWorldHolder;
import org.anjocaido.groupmanager.permissions.AnjoPermissionsHandler;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class GMHandler implements Listener {
	private GroupManager gm;
	
	public GMHandler(NoirlandAutoPromote plugin) {
		final PluginManager pm = plugin.getServer().getPluginManager();
		final Plugin GMplugin = pm.getPlugin("GroupManager");
 
		if (GMplugin != null && GMplugin.isEnabled()) {
			gm = (GroupManager)GMplugin;
 
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPluginDisable(PluginDisableEvent event) {
		if (gm != null) {
			if (event.getPlugin().getDescription().getName().equals("GroupManager")) {
				gm = null;
			}
		}
	}
	
	public String getGroup(final Player player) {
		final AnjoPermissionsHandler handler = gm.getWorldsHolder().getWorldPermissions(player);
		if (handler == null) {
			return null;
		}
		return handler.getGroup(player.getName());
	}
	
	public String getPrefix(final Player player) {
		final AnjoPermissionsHandler handler = gm.getWorldsHolder().getWorldPermissions(player);
		if (handler == null) {
			return null;
		}
		return handler.getGroupPrefix(getGroup(player));
	}
 
	public boolean setGroup(final Player base, final String group) {
		final OverloadedWorldHolder handler = gm.getWorldsHolder().getWorldData(base);
		if (handler == null) {
			return false;
		}
		handler.getUser(base.getName()).setGroup(handler.getGroup(group));
		return true;
	}
}
