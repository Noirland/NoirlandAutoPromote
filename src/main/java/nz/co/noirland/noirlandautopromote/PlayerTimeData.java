package nz.co.noirland.noirlandautopromote;

import nz.co.noirland.noirlandautopromote.tasks.PlayerPromoteTask;

public class PlayerTimeData implements Comparable<PlayerTimeData> {

    private String player;
    private long joined;
    private long playTime;
    private long totalPlayTime;
    private boolean changed = false;
    private boolean online = false;
    private PlayerPromoteTask promoteTask;

    public PlayerTimeData(String player, long playTime, long totalPlayTime) {
        this.player = player;
        this.playTime = playTime;
        this.totalPlayTime = totalPlayTime;
    }

    public void joined() {
        joined = System.currentTimeMillis();
        online = true;
    }

    public void left() {
        updatePlayTime();
        online = false;
    }

    public void updatePlayTime() {
        long time = System.currentTimeMillis();
        playTime += time - joined;
        totalPlayTime += time - joined;
        joined = time;
        changed = true;
    }

    public long getPlayTime() {
        return playTime + (online ? (System.currentTimeMillis() - joined) : 0);
    }

    public void setPlayTime(long playTime) {
        this.playTime = playTime;
    }

    public long getTotalPlayTime() {
        return totalPlayTime + (online ? (System.currentTimeMillis() - joined) : 0);
    }

    public void setTotalPlayTime(long totalPlayTime) {
        this.totalPlayTime = totalPlayTime;
    }

    public String getPlayer() {
        return player;
    }

    public boolean isOnline() {
        return online;
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
