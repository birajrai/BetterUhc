package me.birajrai.threads;

import me.birajrai.BetterUhc;
import me.birajrai.exceptions.UhcPlayerNotOnlineException;
import me.birajrai.languages.Lang;
import me.birajrai.players.PlayerManager;
import me.birajrai.players.UhcPlayer;
import me.birajrai.utils.TimeUtils;
import me.birajrai.utils.UniversalSound;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TimeBeforeSendBungeeThread implements Runnable{

	private final PlayerManager playerManager;
	private final UhcPlayer uhcPlayer;
	private int remainingTime;
	
	public TimeBeforeSendBungeeThread(PlayerManager playerManager, UhcPlayer uhcPlayer, int remainingTime){
		this.playerManager = playerManager;
		this.uhcPlayer = uhcPlayer;
		this.remainingTime = remainingTime;
	}

	@Override
	public void run() {
		remainingTime--;

		Player player;
		try {
			player = uhcPlayer.getPlayer();

			if(remainingTime <=10 || remainingTime%10 == 0){
				player.sendMessage(Lang.PLAYERS_SEND_BUNGEE.replace("%time%",TimeUtils.getFormattedTime(remainingTime)));
				playerManager.playsoundTo(uhcPlayer, UniversalSound.CLICK);
			}

			if(remainingTime <= 0){
				playerManager.sendPlayerToBungeeServer(player);
			}

		} catch (UhcPlayerNotOnlineException e) {
			// nothing to do for offline players
		}

		if(remainingTime > 0){
			Bukkit.getScheduler().runTaskLater(BetterUhc.getPlugin(), this, 20);
		}
	}

}