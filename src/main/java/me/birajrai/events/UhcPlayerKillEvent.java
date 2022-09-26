package me.birajrai.events;

import me.birajrai.players.UhcPlayer;

public final class UhcPlayerKillEvent extends UhcEvent {

	private final UhcPlayer killer, killed;

	public UhcPlayerKillEvent(UhcPlayer killer, UhcPlayer killed){
		this.killer = killer;
		this.killed = killed;
	}

	public UhcPlayer getKiller(){
		return killer;
	}

	public UhcPlayer getKilled(){
		return killed;
	}

}