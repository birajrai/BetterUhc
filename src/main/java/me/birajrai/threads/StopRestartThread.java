package me.birajrai.threads;

import me.birajrai.BetterUhc;
import me.birajrai.configuration.MainConfig;
import me.birajrai.game.GameManager;
import me.birajrai.languages.Lang;
import org.bukkit.Bukkit;

public class StopRestartThread implements Runnable{

	private long timeBeforeStop;
	
	public StopRestartThread(){
		this.timeBeforeStop = GameManager.getGameManager().getConfig().get(MainConfig.TIME_BEFORE_RESTART_AFTER_END);
	}
	
	@Override
	public void run() {
		if (timeBeforeStop < 0){
			return; // Stop thread
		}

		GameManager gm = GameManager.getGameManager();
			
		if(timeBeforeStop == 0){
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "restart");
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop");
		}else{
			if(timeBeforeStop<5 || timeBeforeStop%10 == 0){
				Bukkit.getLogger().info("[BetterUHC] Server will shutdown in "+timeBeforeStop+"s");
				gm.broadcastInfoMessage(Lang.GAME_SHUTDOWN.replace("%time%", ""+timeBeforeStop));
			}

			timeBeforeStop--;
			Bukkit.getScheduler().scheduleSyncDelayedTask(BetterUhc.getPlugin(), this,20);
		}
	}

}