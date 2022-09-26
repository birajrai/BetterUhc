package me.birajrai.threads;

import me.birajrai.BetterUhc;
import me.birajrai.game.GameManager;
import me.birajrai.languages.Lang;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldBorder;

public class WorldBorderThread implements Runnable{

	private long timeBeforeShrink;
	private final long timeToShrink;
	private final int endSize;
	
	public WorldBorderThread(long timeBeforeShrink, int endSize, long timeToShrink){
		this.timeBeforeShrink = timeBeforeShrink;
		this.endSize = endSize;
		this.timeToShrink = timeToShrink;
	}
	
	@Override
	public void run() {
		if(timeBeforeShrink <= 0){
			startMoving();
		}else{
			timeBeforeShrink--;
			Bukkit.getScheduler().runTaskLater(BetterUhc.getPlugin(), this, 20);
		}
	}
	
	private void startMoving(){
		GameManager.getGameManager().broadcastInfoMessage(Lang.GAME_BORDER_START_SHRINKING);
		
		World overworld = GameManager.getGameManager().getMapLoader().getUhcWorld(World.Environment.NORMAL);
		WorldBorder overworldBorder = overworld.getWorldBorder();
		overworldBorder.setSize(2*endSize, timeToShrink);
		
		World nether = GameManager.getGameManager().getMapLoader().getUhcWorld(World.Environment.NETHER);
		if (nether != null) {
			WorldBorder netherBorder = nether.getWorldBorder();
			netherBorder.setSize(endSize, timeToShrink);
		}
	}

}