package me.birajrai.events;

import me.birajrai.players.PlayerState;
import me.birajrai.players.UhcPlayer;

public class UhcPlayerStateChangedEvent extends UhcEvent {

	private final UhcPlayer player;
	private final PlayerState oldPlayerState, newPlayerState;

	public UhcPlayerStateChangedEvent(UhcPlayer player, PlayerState oldPlayerState, PlayerState newPlayerState){
		this.player = player;
		this.oldPlayerState = oldPlayerState;
		this.newPlayerState = newPlayerState;
	}

	public UhcPlayer getPlayer() {
		return player;
	}

	public PlayerState getOldPlayerState() {
		return oldPlayerState;
	}

	public PlayerState getNewPlayerState() {
		return newPlayerState;
	}

}