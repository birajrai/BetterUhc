package me.birajrai.events;

import me.birajrai.players.UhcPlayer;

import java.util.Set;

public class UhcWinEvent extends UhcEvent {

	private final Set<UhcPlayer> winners;
	
	public UhcWinEvent(Set<UhcPlayer> winners){
		this.winners = winners;
	}

	public Set<UhcPlayer> getWinners(){
		return winners;
	}

}