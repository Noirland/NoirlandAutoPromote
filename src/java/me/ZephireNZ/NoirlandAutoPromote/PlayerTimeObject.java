package me.ZephireNZ.NoirlandAutoPromote;

import org.bukkit.entity.Player;

class PlayerTimeObject {
	
	private final Player player;
	private long joinTime;
	private long quitTime;
	
	public PlayerTimeObject(Player player) {
		this.player = player;
		setJoinTime();
	}
	public void setJoinTime() {
		joinTime = System.currentTimeMillis();
	}
	
	public long getJoinTime() {
		return joinTime;
	}
	
	public void setQuitTime() {
		quitTime = System.currentTimeMillis();
	}
	
	public long getQuitTime() {
		return quitTime;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public long getPlayTime() {
        return quitTime - joinTime;
	}
	
	public void resetQuitTime() {
		quitTime = 0;
	}
}
