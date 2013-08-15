package me.ZephireNZ.NoirlandAutoPromote.tasks;

import me.ZephireNZ.NoirlandAutoPromote.NoirlandAutoPromote;
import me.ZephireNZ.NoirlandAutoPromote.PromotionHandler;

public class PromotePlayerTask {

    NoirlandAutoPromote plugin;
    PromotionHandler pmHandler;
    String player;

    public PromotePlayerTask(NoirlandAutoPromote plugin, String player) {
        this.plugin = plugin;
        this.pmHandler = plugin.pmHandler;

    }

    public void run() {

        if(plugin.getServer().getPlayer(player) != null) {
            pmHandler.checkForPromotion(plugin.getServer().getPlayer(player));
        }

    }
}
