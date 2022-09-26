package me.birajrai.scenarios.scenariolisteners;

import me.birajrai.events.UhcStartedEvent;
import me.birajrai.exceptions.UhcPlayerNotOnlineException;
import me.birajrai.scenarios.ScenarioListener;
import me.birajrai.utils.UniversalMaterial;
import org.bukkit.event.EventHandler;

public class FlyHighListener extends ScenarioListener{

    @EventHandler
    public void onGameStart(UhcStartedEvent e){
        getPlayerManager().getOnlinePlayingPlayers().forEach(uhcPlayer -> {
            try{
                uhcPlayer.getPlayer().getInventory().addItem(UniversalMaterial.ELYTRA.getStack());
            }catch (UhcPlayerNotOnlineException ex){
                // No elytra for offline players.
            }
        });
    }

}