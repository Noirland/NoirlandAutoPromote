package me.ZephireNZ.NoirlandAutoPromote.commands;

import me.ZephireNZ.NoirlandAutoPromote.NoirlandAutoPromote;
import org.bukkit.command.CommandSender;

class CommandHelp extends Command {

    public CommandHelp(NoirlandAutoPromote plugin) {
        super(plugin);
    }

    public boolean run(CommandSender sender, String[] args) {
        printHelp(sender, "help");
        return true;
    }
}
