package me.ZephireNZ.NoirlandAutoPromote;

import org.anjocaido.groupmanager.GroupManager;
import org.anjocaido.groupmanager.data.Group;
import org.anjocaido.groupmanager.dataholder.OverloadedWorldHolder;
import org.anjocaido.groupmanager.permissions.AnjoPermissionsHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.util.Map;

public class GMHandler {
	private GroupManager gm;
	private final ConfigHandler confHandler;

	public GMHandler(NoirlandAutoPromote plugin) {
		final PluginManager pm = plugin.getServer().getPluginManager();
		final Plugin GMplugin = pm.getPlugin("GroupManager");
		confHandler = plugin.confHandler;
 
		if (GMplugin != null && GMplugin.isEnabled()) {
			gm = (GroupManager)GMplugin;
		}
		
	}
	
	public String getGroup(final Player player) {
		final AnjoPermissionsHandler handler = gm.getWorldsHolder().getWorldPermissions(player);
		if (handler == null) {
			return null;
		}
        return handler.getGroup(player.getName());
	}

    public String getGroup(final String player) {
        final OverloadedWorldHolder handler = gm.getWorldsHolder().getDefaultWorld();
        if(handler == null) {
            return null;
        }
        return handler.getUser(player).getGroupName();
    }
	
	public String getColor(final String player, boolean next) {
		final AnjoPermissionsHandler handler = gm.getWorldsHolder().getWorldPermissions(player);
		String color;
		if (handler == null) {
			return null;
		}
		if(next) {
			String nextRank = confHandler.getPromoteTo(getGroup(player));
			color = ChatColor.translateAlternateColorCodes("&".charAt(0), handler.getGroupPrefix(nextRank));
		}else{
			color = ChatColor.translateAlternateColorCodes("&".charAt(0), handler.getGroupPrefix(getGroup(player)));
		}
		return color;
	}

    public String getColor(final Player player, boolean next) {
        return getColor(player.getName(), next);
    }

    public String getGroupColor(final String group) {
        final AnjoPermissionsHandler handler = gm.getWorldsHolder().getDefaultWorld().getPermissionsHandler();
        if (handler == null) {
            return null;
        }

        return ChatColor.translateAlternateColorCodes("&".charAt(0), handler.getGroupPrefix(group));
    }
 
	public boolean setGroup(final Player player, final String group) {
		final OverloadedWorldHolder handler = gm.getWorldsHolder().getWorldData(player);
        Group newGroup;
		if (handler == null) {
            return false;
		}
        if(isGroup(group)) {
            newGroup = handler.getGroup(group);
            handler.getUser(player.getName()).setGroup(newGroup);
            return true;
        }else{
            return false;
        }
    }

    boolean isGroup(String group) {
        final OverloadedWorldHolder handler = gm.getWorldsHolder().getDefaultWorld();
        if (handler == null) {
            return false;
        }
        Map<String, Group> groups = handler.getGroups();
        return groups.containsKey(group);
    }
}
