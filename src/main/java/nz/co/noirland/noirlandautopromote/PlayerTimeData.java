package nz.co.noirland.noirlandautopromote;

import nz.co.noirland.noirlandautopromote.tasks.PlayerPromoteTask;
import nz.co.noirland.zephcore.Util;

import java.util.UUID;

public class PlayerTimeData implements Comparable<PlayerTimeData> {

    private final UUID player;
    private long joined;
    private long playTime;
    private long totalPlayTime;
    private boolean changed = false;
    private PlayerPromoteTask promoteTask;

    public PlayerTimeData(UUID player, long playTime, long totalPlayTime) {
        this.player = player;
        this.playTime = playTime;
        this.totalPlayTime = totalPlayTime;
        joined = System.currentTimeMillis();
    }

    public void joined() {
        joined = System.currentTimeMillis();
    }

    public void left() {
        updatePlayTime();
    }

    public void updatePlayTime() {
        long time = System.currentTimeMillis();
        playTime += time - joined;
        totalPlayTime += time - joined;
        joined = time;
        changed = true;
    }

    public long getPlayTime() {
        return playTime + (Util.player(player).isOnline() ? (System.currentTimeMillis() - joined) : 0);
    }

    public void setPlayTime(long playTime) {
        this.playTime = playTime;
    }

    public long getTotalPlayTime() {
        return totalPlayTime + (Util.player(player).isOnline() ? (System.currentTimeMillis() - joined) : 0);
    }

    public void setTotalPlayTime(long totalPlayTime) {
        this.totalPlayTime = totalPlayTime;
    }

    public UUID getPlayer() {
        return player;
    }

    public boolean isChanged() {
        return changed;
    }

    public PlayerPromoteTask getPromoteTask() {
        return promoteTask;
    }

    public void setPromoteTask(PlayerPromoteTask promoteTask) {
        this.promoteTask = promoteTask;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    @Override
    public int compareTo(PlayerTimeData other) {
        if(getTotalPlayTime() > other.getTotalPlayTime()) return -1;
        if(getTotalPlayTime() < other.getTotalPlayTime()) return 1;
        return 0;
    }
}
