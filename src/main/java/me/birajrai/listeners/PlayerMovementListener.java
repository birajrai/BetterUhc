package me.birajrai.listeners;

import me.birajrai.players.PlayerManager;
import me.birajrai.players.UhcPlayer;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMovementListener implements Listener{

    private final PlayerManager playerManager;

    public PlayerMovementListener(PlayerManager playerManager){
        this.playerManager = playerManager;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event){
        handleFrozenPlayers(event);
    }

    private void handleFrozenPlayers(PlayerMoveEvent e){
        UhcPlayer uhcPlayer = playerManager.getUhcPlayer(e.getPlayer());
        if (uhcPlayer.isFrozen()){
            Location freezeLoc = uhcPlayer.getFreezeLocation();
            Location toLoc = e.getTo();

            if (toLoc.getBlockX() != freezeLoc.getBlockX() || toLoc.getBlockZ() != freezeLoc.getBlockZ()){
                Location newLoc = toLoc.clone();
                newLoc.setX(freezeLoc.getBlockX() + .5);
                newLoc.setZ(freezeLoc.getBlockZ() + .5);

                e.getPlayer().teleport(newLoc);
            }
        }
    }

}