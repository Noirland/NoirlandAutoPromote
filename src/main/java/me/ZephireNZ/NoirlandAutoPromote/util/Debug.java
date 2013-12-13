package me.ZephireNZ.NoirlandAutoPromote.util;

import me.ZephireNZ.NoirlandAutoPromote.NoirlandAutoPromote;
import me.ZephireNZ.NoirlandAutoPromote.config.PluginConfig;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Logger;

public class Debug {

    private static NoirlandAutoPromote plugin = NoirlandAutoPromote.inst();
    private static Logger logger = plugin.getLogger();

    /**
     * Show a debug message if debug is true in config.
     * @param msg message to be shown in console
     */
    public static void debug(String msg) {

        if(PluginConfig.inst().getDebug()) {
            logger.info("[DEBUG] " + msg);
        }

    }

    /**
     * Show an Exception's stack trace in console if debug is true.
     * @param e execption to be shown
     */
    public static void debug(Throwable e) {
        debug(ExceptionUtils.getStackTrace(e));
    }

    /**
     * Show both a debug message and a stacktrace
     * @param msg message to be shown in console
     * @param e execption to be shown
     */
    public static void debug(String msg, Throwable e) {
        debug(msg);
        debug(e);
    }

    public static void sendMessage(CommandSender sender, String msg, Boolean prefix) {
        if(prefix) {
            msg = ChatColor.RED + "[NoirPromote] " + ChatColor.RESET + msg;
        }
        if(sender instanceof Player) {
            Player pSender = (Player) sender;
            pSender.sendMessage(msg);
        }else{
            logger.info(ChatColor.stripColor(msg));
        }
    }

}
