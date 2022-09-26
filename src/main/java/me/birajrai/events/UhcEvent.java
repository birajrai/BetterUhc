package me.birajrai.events;

import me.birajrai.game.GameManager;
import me.birajrai.players.PlayerManager;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public abstract class UhcEvent extends Event{

    private static HandlerList handlers;

    static{
        handlers = new HandlerList();
    }

    @Override
    public HandlerList getHandlers(){
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public GameManager getGameManager(){
        return GameManager.getGameManager();
    }

    public PlayerManager getPlayerManager(){
        return getGameManager().getPlayerManager();
    }

}