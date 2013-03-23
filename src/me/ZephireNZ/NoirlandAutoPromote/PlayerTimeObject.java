package me.ZephireNZ.NoirlandAutoPromote;

import java.util.concurrent.TimeUnit;

import org.bukkit.entity.Player;

public class PlayerTimeObject {
	
	private Player player;
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
	
	public String getPlayTime() {
		long playTime = quitTime - joinTime;
		String strPlayTime = String.format("%d min, %d sec", 
			    TimeUnit.MILLISECONDS.toMinutes(playTime),
			    TimeUnit.MILLISECONDS.toSeconds(playTime) - 
			    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(playTime))
		);
		return strPlayTime;
	}
}
