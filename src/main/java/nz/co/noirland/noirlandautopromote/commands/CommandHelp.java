package nz.co.noirland.noirlandautopromote.commands;

import org.bukkit.command.CommandSender;

class CommandHelp extends Command {

    public boolean run(CommandSender sender, String[] args) {
        printHelp(sender, "help");
        return true;
    }
}
