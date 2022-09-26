package me.birajrai.listeners;

import me.birajrai.BetterUhc;
import me.birajrai.game.GameManager;
import me.birajrai.game.handlers.PlayerDeathHandler;
import me.birajrai.players.PlayerManager;
import me.birajrai.players.PlayerState;
import me.birajrai.players.UhcPlayer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerDeathListener implements Listener {

	private final PlayerDeathHandler playerDeathHandler;

	public PlayerDeathListener(PlayerDeathHandler playerDeathHandler) {
		this.playerDeathHandler = playerDeathHandler;
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerDeath(PlayerDeathEvent event) {
		playerDeathHandler.handlePlayerDeath(event);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerRespawn(PlayerRespawnEvent event){
		PlayerManager pm = GameManager.getGameManager().getPlayerManager();
		UhcPlayer uhcPlayer = pm.getUhcPlayer(event.getPlayer());

		if(uhcPlayer.getState().equals(PlayerState.DEAD)){
			Bukkit.getScheduler().runTaskLater(BetterUhc.getPlugin(), () -> pm.setPlayerSpectateAtLobby(uhcPlayer), 1);
		}
	}
	
}