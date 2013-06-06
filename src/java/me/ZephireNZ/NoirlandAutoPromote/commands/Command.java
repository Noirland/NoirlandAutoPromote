package me.ZephireNZ.NoirlandAutoPromote.commands;

import me.ZephireNZ.NoirlandAutoPromote.*;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

public class Command implements CommandExecutor {

    final NoirlandAutoPromote plugin;
    final GMHandler gmHandler;
    final DatabaseHandler dbHandler;
    final ConfigHandler confHandler;
    final PromotionHandler pmHandler;
    private final Map<String, Command> commands = new HashMap<String, Command>();
    private Command fallback;
    boolean creatingCommands = false;
    private final Map<String, String> commandHelp = new HashMap <String, String>();

    public Command(NoirlandAutoPromote plugin) {
        this.plugin = plugin;
        this.gmHandler = plugin.gmHandler;
        this.dbHandler = plugin.dbHandler;
        this.confHandler = plugin.confHandler;
        this.pmHandler = plugin.pmHandler;

        commandHelp.put("check", ChatColor.GRAY + "/autopromote " + ChatColor.RESET +                           "Show your promotion stats.");
        commandHelp.put("check.others", ChatColor.GRAY + "/autopromote [player] " + ChatColor.RESET +           "Show another's promotion stats.");
        commandHelp.put("reload", ChatColor.GRAY + "/autopromote reload " + ChatColor.RESET +                   "Reload the plugin.");
        commandHelp.put("promote", ChatColor.GRAY + "/autopromote promote [player] (rank) " + ChatColor.RESET + "Promote a player (optionally to specified rank).");
        commandHelp.put("reset", ChatColor.GRAY + "/autopromote reset [player] (hours) " + ChatColor.RESET +    "Reset a player's play time, 0 hours by default.");

    }

    boolean run(CommandSender sender, String[] args) {
        return false;
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {

        fillCommands();

        if(args.length > 0 && commands.containsKey(args[0].toLowerCase())) {
            return commands.get(args[0]).run(sender, args);
        }else{
            return fallback.run(sender, args);
        }


    }

    private void fillCommands() {
        if(commands.isEmpty()){
        commands.put("top", new CommandTop(plugin));
        commands.put("reload", new CommandReload(plugin));
        commands.put("reset", new CommandReset(plugin));
        commands.put("promote", new CommandPromote(plugin));
        commands.put("help", new CommandHelp(plugin));
        fallback = new CommandFallback(plugin);
        }
    }

    void printHelp(CommandSender sender, String command) {
//        plugin.sendMessage(sender, "==== " + ChatColor.RED + "NoirPromote" + ChatColor.RESET + " ====");

//        for(Map.Entry<String, String> perm : perms.entrySet()) {
//            if(sender.hasPermission(commands.getKey())) {
//                plugin.sendMessage(sender, commands.getValue());
//            }
//        }
        if(command.equals("help")) {
            plugin.sendMessage(sender, "==== " + ChatColor.RED + "NoirPromote" + ChatColor.RESET + " ====");

            for(Map.Entry<String, String> entry : commandHelp.entrySet()) {
                if(sender.hasPermission(entry.getKey())) {
                    plugin.sendMessage(sender, entry.getValue());
                }
            }
        }else{
            for(Map.Entry<String, String> entry : commandHelp.entrySet()) {
                if(command.equals(entry.getKey())) {
                    plugin.sendMessage(sender, entry.getValue());
                }
            }
        }
    }
}
