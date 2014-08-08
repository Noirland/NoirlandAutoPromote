package nz.co.noirland.noirlandautopromote.callbacks;

import nz.co.noirland.noirlandautopromote.GMHandler;
import nz.co.noirland.noirlandautopromote.NoirlandAutoPromote;
import nz.co.noirland.noirlandautopromote.PlayerTimeData;
import nz.co.noirland.zephcore.Callback;
import nz.co.noirland.zephcore.Util;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.*;

public class TopCallback extends Callback {

    Map<UUID, String> names = new HashMap<UUID, String>();
    List<PlayerTimeData> data;
    CommandSender sender;
    int page;
    int numPages;
    int start;

    public TopCallback(CommandSender sender, List<PlayerTimeData> data, int page, int numPages, int start) {
        super(NoirlandAutoPromote.inst());
        this.data = data;
        this.sender = sender;
        this.start = start;
        this.numPages = numPages;
        this.page = page;
        List<UUID> uuids = new ArrayList<UUID>();

        for(PlayerTimeData pData : data) {
            uuids.add(pData.getPlayer());
        }
        new TopThread(uuids);
    }

    @Override
    public void run() {
        NoirlandAutoPromote plugin = NoirlandAutoPromote.inst();
        GMHandler gmHandler = GMHandler.inst();

        plugin.sendMessage(sender,"==== " + ChatColor.RED + "NoirPromote" + ChatColor.RESET + " ====", false);


        plugin.sendMessage(sender,"Page " + ChatColor.GOLD + page + ChatColor.RESET + " of " + ChatColor.GOLD + numPages, false);

        int rank = start;

        ArrayList<String> ranks = new ArrayList<String>();
        for(PlayerTimeData data : this.data) {
            String name = names.get(data.getPlayer());
            String color = gmHandler.getGroupColor(gmHandler.getGroup(name));
            if(color == null) {
                color = ChatColor.RESET.toString();
            }
            String msg = ++rank + ". " + color + name + ChatColor.RESET + ": " + Util.formatTime(data.getTotalPlayTime());
            ranks.add(msg);
        }
        for(String msg : ranks) {
            plugin.sendMessage(sender, msg, false);

        }
    }

    private class TopThread implements Runnable {

        List<UUID> uuids;

        private TopThread(List<UUID> players) {
            this.uuids = players;
            new Thread(this, "AutoPromote-TopThread").start();
        }

        @Override
        public void run() {
            for(UUID uuid : uuids) {
                names.put(uuid, Util.name(uuid));
            }
            schedule();
        }
    }

}
