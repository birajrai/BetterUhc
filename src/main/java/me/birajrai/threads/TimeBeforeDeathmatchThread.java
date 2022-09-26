package me.birajrai.threads;

import me.birajrai.BetterUhc;
import me.birajrai.game.GameManager;
import me.birajrai.game.GameState;
import me.birajrai.game.handlers.DeathmatchHandler;
import me.birajrai.utils.TimeUtils;
import me.birajrai.utils.UniversalSound;
import org.bukkit.Bukkit;

public class TimeBeforeDeathmatchThread implements Runnable{

	private final GameManager gameManager;
	private final DeathmatchHandler deathmatchHandler;

	public TimeBeforeDeathmatchThread(GameManager gameManager, DeathmatchHandler deathmatchHandler) {
		this.gameManager = gameManager;
		this.deathmatchHandler = deathmatchHandler;
	}
	
	@Override
	public void run() {
		long remainingTime = gameManager.getRemainingTime();

		remainingTime--;
		gameManager.setRemainingTime(remainingTime);
		
		if(remainingTime >= 0 && remainingTime <= 60 && (remainingTime%10 == 0 || remainingTime <= 10)){
			gameManager.getPlayerManager().playSoundToAll(UniversalSound.CLICK);
		}

		if (remainingTime == 0){
			deathmatchHandler.startDeathmatch();
		}else if(remainingTime > 0 && gameManager.getGameState() == GameState.PLAYING) {
			Bukkit.getScheduler().runTaskLater(BetterUhc.getPlugin(), this, TimeUtils.SECOND_TICKS);
		}
	}
	
}
