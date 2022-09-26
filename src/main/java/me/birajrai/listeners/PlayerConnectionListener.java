package me.birajrai.listeners;

import me.birajrai.BetterUhc;
import me.birajrai.configuration.MainConfig;
import me.birajrai.exceptions.UhcPlayerJoinException;
import me.birajrai.exceptions.UhcTeamException;
import me.birajrai.game.GameManager;
import me.birajrai.game.GameState;
import me.birajrai.game.handlers.PlayerDeathHandler;
import me.birajrai.game.handlers.ScoreboardHandler;
import me.birajrai.players.PlayerManager;
import me.birajrai.players.PlayerState;
import me.birajrai.players.UhcPlayer;
import me.birajrai.threads.KillDisconnectedPlayerThread;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerConnectionListener implements Listener{

	private final GameManager gameManager;
	private final PlayerManager playerManager;
	private final PlayerDeathHandler playerDeathHandler;
	private final ScoreboardHandler scoreboardHandler;

	public PlayerConnectionListener(GameManager gameManager, PlayerManager playerManager, PlayerDeathHandler playerDeathHandler, ScoreboardHandler scoreboardHandler){
		this.gameManager = gameManager;
		this.playerManager = playerManager;
		this.playerDeathHandler = playerDeathHandler;
		this.scoreboardHandler = scoreboardHandler;
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onPlayerLogin(PlayerLoginEvent event){
		// Player is not allowed to join so don't create UhcPlayer. (Server full, whitelist, ban, ...)
		if (event.getResult() != Result.ALLOWED){
			return;
		}
		
		try{
			boolean allowedToJoin = playerManager.isPlayerAllowedToJoin(event.getPlayer());

			if (allowedToJoin){
				// Create player if not existent.
				playerManager.getOrCreateUhcPlayer(event.getPlayer());
			}else{
				throw new UhcPlayerJoinException("An unexpected error as occured.");
			}
		}catch(final UhcPlayerJoinException e){
			event.setKickMessage(e.getMessage());
			event.setResult(Result.KICK_OTHER);
		}
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onPlayerJoin(final PlayerJoinEvent event){
		Bukkit.getScheduler().runTaskLater(BetterUhc.getPlugin(), () -> playerManager.playerJoinsTheGame(event.getPlayer()), 1);
	}

	@EventHandler(priority=EventPriority.HIGHEST)
	public void onPlayerDisconnect(PlayerQuitEvent event){
		if(gameManager.getGameState().equals(GameState.WAITING) || gameManager.getGameState().equals(GameState.STARTING)){
			UhcPlayer uhcPlayer = playerManager.getUhcPlayer(event.getPlayer());

			if(gameManager.getGameState().equals(GameState.STARTING)){
				playerManager.setPlayerSpectateAtLobby(uhcPlayer);
				gameManager.broadcastInfoMessage(uhcPlayer.getName()+" has left while the game was starting and has been killed.");
				if (gameManager.getConfig().get(MainConfig.STRIKE_LIGHTNING_ON_DEATH)) {
					playerManager.strikeLightning(uhcPlayer);
				}
			}

			try{
				uhcPlayer.getTeam().leave(uhcPlayer);

				// Update player tab
				scoreboardHandler.updatePlayerOnTab(uhcPlayer);
			}catch (UhcTeamException e){
				// Nothing
			}

			playerManager.getPlayersList().remove(uhcPlayer);
		}

		if(gameManager.getGameState().equals(GameState.PLAYING) || gameManager.getGameState().equals(GameState.DEATHMATCH)){
			UhcPlayer uhcPlayer = playerManager.getUhcPlayer(event.getPlayer());
			if(gameManager.getConfig().get(MainConfig.ENABLE_KILL_DISCONNECTED_PLAYERS) && uhcPlayer.getState().equals(PlayerState.PLAYING)){

				KillDisconnectedPlayerThread killDisconnectedPlayerThread = new KillDisconnectedPlayerThread(
                        playerDeathHandler, event.getPlayer().getUniqueId(),
						gameManager.getConfig().get(MainConfig.MAX_DISCONNECT_PLAYERS_TIME)
				);

				Bukkit.getScheduler().runTaskLaterAsynchronously(BetterUhc.getPlugin(), killDisconnectedPlayerThread,1);
			}
			if(gameManager.getConfig().get(MainConfig.SPAWN_OFFLINE_PLAYERS) && uhcPlayer.getState().equals(PlayerState.PLAYING)){
				playerManager.spawnOfflineZombieFor(event.getPlayer());
			}
			playerManager.checkIfRemainingPlayers();
		}
	}

}