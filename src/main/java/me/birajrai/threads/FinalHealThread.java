package me.birajrai.threads;

import me.birajrai.exceptions.UhcPlayerNotOnlineException;
import me.birajrai.game.GameManager;
import me.birajrai.languages.Lang;
import me.birajrai.players.PlayerManager;
import me.birajrai.players.UhcPlayer;
import org.bukkit.entity.Player;

public class FinalHealThread implements Runnable{

	private final GameManager gameManager;
	private final PlayerManager playerManager;

	public FinalHealThread(GameManager gameManager, PlayerManager playerManager){
		this.gameManager = gameManager;
		this.playerManager = playerManager;
	}
	
	@Override
	public void run() {

		for (UhcPlayer uhcPlayer : playerManager.getOnlinePlayingPlayers()){
			try {
				Player bukkitPlayer = uhcPlayer.getPlayer();
				bukkitPlayer.setHealth(bukkitPlayer.getMaxHealth());
			}catch (UhcPlayerNotOnlineException ex){
				// no heal for offline players
			}
		}

		gameManager.broadcastInfoMessage(Lang.GAME_FINAL_HEAL);
	}

}