package me.ZephireNZ.NoirlandAutoPromote.tasks;

import me.ZephireNZ.NoirlandAutoPromote.commands.CommandFemale;
import org.bukkit.scheduler.BukkitRunnable;

public class VerifyFemaleTask extends BukkitRunnable {

    CommandFemale command;
    String player;

    public VerifyFemaleTask(CommandFemale commandFemale, String player) {
        this.command = commandFemale;
        this.player = player;
    }

    public void run() {

        if(command.awaitingVerification.contains(player)) {
            command.awaitingVerification.remove(player);
        }
    }
}
