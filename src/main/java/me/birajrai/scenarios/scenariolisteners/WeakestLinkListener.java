package me.birajrai.scenarios.scenariolisteners;

import me.birajrai.events.UhcTimeEvent;
import me.birajrai.exceptions.UhcPlayerNotOnlineException;
import me.birajrai.languages.Lang;
import me.birajrai.players.UhcPlayer;
import me.birajrai.scenarios.Option;
import me.birajrai.scenarios.ScenarioListener;
import me.birajrai.utils.VersionUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class WeakestLinkListener extends ScenarioListener{

    @Option
    private long delay = 10*60;

    @EventHandler
    public void onUhcTime(UhcTimeEvent e){
        // Run every minute
        if (e.getTotalTime() % delay != 0){
            return;
        }

        UhcPlayer lowest = getLowestHealthPlayer();

        // Two or more players share the lowest health.
        if (lowest == null){
            return;
        }

        // Kill player
        try {
            Player player = lowest.getPlayer();
            VersionUtils.getVersionUtils().killPlayer(player);
        }catch (UhcPlayerNotOnlineException ex){
            ex.printStackTrace();
        }

        getGameManager().broadcastMessage(Lang.SCENARIO_WEAKESTLINK_KILL.replace("%player%", lowest.getName()));
    }

    private UhcPlayer getLowestHealthPlayer(){
        UhcPlayer lowestPlayer = null;
        double lowestHealth = 0;

        // Look for player with lowest health.
        for (UhcPlayer uhcPlayer : getPlayerManager().getOnlinePlayingPlayers()){
            try {
                if (lowestPlayer == null){
                    lowestPlayer = uhcPlayer;
                    lowestHealth = uhcPlayer.getPlayer().getHealth();
                }else {
                    double playerHealth = uhcPlayer.getPlayer().getHealth();
                    if (playerHealth < lowestHealth){
                        lowestPlayer = uhcPlayer;
                        lowestHealth = playerHealth;
                    }
                }
            }catch (UhcPlayerNotOnlineException ex){
                ex.printStackTrace();
            }
        }

        // Check for player with same health.
        for (UhcPlayer uhcPlayer : getPlayerManager().getOnlinePlayingPlayers()){
            // Don't check itself
            if (lowestPlayer == uhcPlayer){
                continue;
            }

            // Check for player with same health, if it exists return null.
            try {
                if (lowestHealth == uhcPlayer.getPlayer().getHealth()){
                    return null;
                }
            }catch (UhcPlayerNotOnlineException ex){
                ex.printStackTrace();
            }
        }

        return lowestPlayer;
    }

}