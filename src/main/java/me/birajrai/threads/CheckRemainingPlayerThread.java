package me.birajrai.threads;

import me.birajrai.BetterUhc;
import me.birajrai.game.GameManager;
import me.birajrai.game.GameState;
import org.bukkit.Bukkit;


public class CheckRemainingPlayerThread implements Runnable{

	private final GameManager gameManager;

	public CheckRemainingPlayerThread(GameManager gameManager){
		this.gameManager = gameManager;
	}

	@Override
	public void run() {
		gameManager.getPlayerManager().checkIfRemainingPlayers();
		GameState state = gameManager.getGameState();

		if(state.equals(GameState.PLAYING) || state.equals(GameState.DEATHMATCH)) {
			Bukkit.getScheduler().runTaskLater(BetterUhc.getPlugin(), this, 40);
		}
	}

}