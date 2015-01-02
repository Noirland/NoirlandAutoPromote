package nz.co.noirland.noirlandautopromote.callbacks;

import com.google.common.util.concurrent.FutureCallback;
import nz.co.noirland.noirlandautopromote.GMHandler;
import nz.co.noirland.noirlandautopromote.NoirlandAutoPromote;
import nz.co.noirland.noirlandautopromote.PlayerTimeData;
import nz.co.noirland.zephcore.Util;
import nz.co.noirland.zephcore.callbacks.GetNamesCallback;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TopCallback {

    public TopCallback(CommandSender sender, List<PlayerTimeData> players, int page, int numPages, int start) {
        List<UUID> uuids = new ArrayList<UUID>();

        for(PlayerTimeData data : players) {
            uuids.add(data.getPlayer());
        }

        new GetNamesCallback(new TopResult(sender, players, page, numPages, start), uuids);
    }

}

class TopResult implements FutureCallback<Map<UUID, String>> {
    final CommandSender to;
    final List<PlayerTimeData> players;
    final int page;
    final int numPages;
    final int start;

    public TopResult(CommandSender to, List<PlayerTimeData> players, int page, int numPages, int start) {
        this.to = to;
        this.players = players;
        this.page = page;
        this.numPages = numPages;
        this.start = start;
    }

    @Override
    public void onSuccess(Map<UUID, String> names) {
        NoirlandAutoPromote plugin = NoirlandAutoPromote.inst();
        GMHandler gmHandler = GMHandler.inst();

        plugin.sendMessage(to,"==== " + ChatColor.RED + "NoirPromote" + ChatColor.RESET + " ====", false);
        plugin.sendMessage(to,"Page " + ChatColor.GOLD + page + ChatColor.RESET + " of " + ChatColor.GOLD + numPages, false);

        int rank = start;

        ArrayList<String> rankings = new ArrayList<String>();
        for(PlayerTimeData data : players) {
            String name = names.get(data.getPlayer());
            String color = gmHandler.getGroupColor(gmHandler.getGroup(name));
            if(color == null) {
                color = ChatColor.RESET.toString();
            }
            String msg = ++rank + ". " + color + name + ChatColor.RESET + ": " + Util.formatTime(data.getTotalPlayTime());
            rankings.add(msg);
        }
        for(String msg : rankings) {
            plugin.sendMessage(to, msg, false);
        }
    }

    @Override
    public void onFailure(Throwable error) {
        to.sendMessage(ChatColor.DARK_RED + "Internal error occurred!");
        NoirlandAutoPromote.debug().warning("Could not read top rankings for page " + page, error);
    }
}
