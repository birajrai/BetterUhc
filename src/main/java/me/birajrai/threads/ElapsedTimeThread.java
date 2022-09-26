package me.birajrai.threads;

import me.birajrai.BetterUhc;
import me.birajrai.events.UhcTimeEvent;
import me.birajrai.game.GameManager;
import me.birajrai.game.GameState;
import me.birajrai.game.handlers.CustomEventHandler;
import me.birajrai.players.UhcPlayer;
import org.bukkit.Bukkit;

import java.util.Set;

public class ElapsedTimeThread implements Runnable{

	private final GameManager gameManager;
	private final CustomEventHandler customEventHandler;
	private final ElapsedTimeThread task;
	
	public ElapsedTimeThread(GameManager gameManager, CustomEventHandler customEventHandler) {
		this.gameManager = gameManager;
		this.customEventHandler = customEventHandler;
		this.task = this;
	}
	
	@Override
	public void run() {
		
		long time = gameManager.getElapsedTime() + 1;
		gameManager.setElapsedTime(time);

		Set<UhcPlayer> playingPlayers = gameManager.getPlayerManager().getOnlinePlayingPlayers();

		// Call time event
		UhcTimeEvent event = new UhcTimeEvent(playingPlayers,time);
		Bukkit.getScheduler().runTask(BetterUhc.getPlugin(), () -> Bukkit.getServer().getPluginManager().callEvent(event));

		customEventHandler.handleTimeEvent(playingPlayers, time);

		if(!gameManager.getGameState().equals(GameState.ENDED)){
			Bukkit.getScheduler().runTaskLater(BetterUhc.getPlugin(), task, 20);
		}
	}

}