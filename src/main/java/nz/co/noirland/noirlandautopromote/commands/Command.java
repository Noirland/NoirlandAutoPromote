package nz.co.noirland.noirlandautopromote.commands;

import nz.co.noirland.noirlandautopromote.GMHandler;
import nz.co.noirland.noirlandautopromote.NoirlandAutoPromote;
import nz.co.noirland.noirlandautopromote.PromotionHandler;
import nz.co.noirland.noirlandautopromote.config.PluginConfig;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

public class Command implements CommandExecutor {

    protected final NoirlandAutoPromote plugin = NoirlandAutoPromote.inst();
    protected final GMHandler gmHandler = GMHandler.inst();
    protected final PluginConfig config = PluginConfig.inst();
    protected final PromotionHandler pmHandler = PromotionHandler.inst();

    protected static Command fallback;
    protected static final Map<String, Command> commands = new HashMap<String, Command>();
    protected static final Map<String, String> commandHelp = new HashMap <String, String>();

    static {
        commandHelp.put("check", ChatColor.GRAY + "/autopromote " + ChatColor.RESET +                           "Show your promotion stats.");
        commandHelp.put("check.others", ChatColor.GRAY + "/autopromote [player] " + ChatColor.RESET +           "Show another's promotion stats.");
        commandHelp.put("check.top", ChatColor.GRAY + "/autopromote top (page) " + ChatColor.RESET +                   "List highest total play times, in groups of 10.");
        commandHelp.put("reload", ChatColor.GRAY + "/autopromote reload " + ChatColor.RESET +                   "Reload the plugin.");
        commandHelp.put("promote", ChatColor.GRAY + "/autopromote promote [player] (rank) " + ChatColor.RESET + "Promote a player (optionally to specified rank).");
        commandHelp.put("reset", ChatColor.GRAY + "/autopromote reset [player] (hours) " + ChatColor.RESET +    "Reset a player's play time, 0 hours by default.");

        if(commands.isEmpty()){
            commands.put("top", new CommandTop());
            commands.put("reload", new CommandReload());
            commands.put("reset", new CommandReset());
            commands.put("promote", new CommandPromote());
            commands.put("help", new CommandHelp());
            fallback = new CommandFallback();
        }
    }

    boolean run(CommandSender sender, String[] args) {
        return false;
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {


        if(args.length > 0 && commands.containsKey(args[0].toLowerCase())) {
            return commands.get(args[0]).run(sender, args);
        }else{
            return fallback.run(sender, args);
        }


    }

    void printHelp(CommandSender sender, String command) {
        if(command.equals("help")) {
            plugin.sendMessage(sender, "==== " + ChatColor.RED + "NoirPromote" + ChatColor.RESET + " ====", false);

            for(Map.Entry<String, String> entry : commandHelp.entrySet()) {
                if(sender.hasPermission("autopromote." + entry.getKey())) {
                    plugin.sendMessage(sender, entry.getValue(), false);
                }
            }
        }else{
            for(Map.Entry<String, String> entry : commandHelp.entrySet()) {
                if(command.equals(entry.getKey())) {
                    plugin.sendMessage(sender, entry.getValue(), false);
                }
            }
        }
    }
}
