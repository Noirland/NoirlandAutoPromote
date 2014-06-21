package nz.co.noirland.noirlandautopromote;

import nz.co.noirland.noirlandautopromote.tasks.PlayerPromoteTask;

import java.util.UUID;

public class PlayerTimeData implements Comparable<PlayerTimeData> {

    private final UUID player;
    private Long join;
    private long playTime;
    private long totalPlayTime;
    private PlayerPromoteTask promoteTask;

    public PlayerTimeData(UUID player, long playTime, long totalPlayTime) {
        this.player = player;
        this.playTime = playTime;
        this.totalPlayTime = totalPlayTime;
        join = null;
    }

    public void joined() {
        join = System.currentTimeMillis();
    }

    public void left() {
        playTime = getPlayTime();
        totalPlayTime = getTotalPlayTime();
        join = null;
    }

    public boolean hasChanged() {
        return playTime != getPlayTime() || totalPlayTime != getTotalPlayTime();
    }

    public long getPlayTime() {
        return playTime + (join != null ? (System.currentTimeMillis() - join) : 0);
    }

    public void setPlayTime(long playTime) {
        this.playTime = playTime;
    }

    public long getTotalPlayTime() {
        return totalPlayTime + (join != null ? (System.currentTimeMillis() - join) : 0);
    }

    public void setTotalPlayTime(long totalPlayTime) {
        this.totalPlayTime = totalPlayTime;
    }

    public UUID getPlayer() {
        return player;
    }

    public PlayerPromoteTask getPromoteTask() {
        return promoteTask;
    }

    public void setPromoteTask(PlayerPromoteTask promoteTask) {
        this.promoteTask = promoteTask;
    }

    @Override
    public int compareTo(PlayerTimeData other) {
        if(getTotalPlayTime() > other.getTotalPlayTime()) return -1;
        if(getTotalPlayTime() < other.getTotalPlayTime()) return 1;
        return 0;
    }
}
