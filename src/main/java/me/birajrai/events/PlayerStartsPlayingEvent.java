package me.birajrai.events;

import me.birajrai.players.UhcPlayer;

/**
 * Event gets called when a player starts playing. This includes when someone gets revived.
 */
public class PlayerStartsPlayingEvent extends UhcEvent{

    private final UhcPlayer uhcPlayer;

    public PlayerStartsPlayingEvent(UhcPlayer uhcPlayer){
        this.uhcPlayer = uhcPlayer;
    }

    public UhcPlayer getUhcPlayer() {
        return uhcPlayer;
    }

}