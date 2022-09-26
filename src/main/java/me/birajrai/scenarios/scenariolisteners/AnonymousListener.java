package me.birajrai.scenarios.scenariolisteners;

import me.birajrai.configuration.Dependencies;
import me.birajrai.events.PlayerStartsPlayingEvent;
import me.birajrai.events.UhcGameStateChangedEvent;
import me.birajrai.events.UhcPlayerStateChangedEvent;
import me.birajrai.game.GameState;
import me.birajrai.players.PlayerState;
import me.birajrai.players.UhcPlayer;
import me.birajrai.scenarios.Scenario;
import me.birajrai.scenarios.ScenarioListener;
import me.birajrai.utils.ProtocolUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;

public class AnonymousListener extends ScenarioListener{

    @Override
    public void onEnable(){
        if (!Dependencies.getProtocolLibLoaded()){
            Bukkit.broadcastMessage(ChatColor.RED + "[UhcCore] For Anonymous ProtocolLib needs to be installed!");
            getScenarioManager().disableScenario(Scenario.ANONYMOUS);
            return;
        }

        for (UhcPlayer uhcPlayer : getPlayerManager().getAllPlayingPlayers()){
            ProtocolUtils.setPlayerNickName(uhcPlayer, getPlayerNickName(uhcPlayer.getName()));
            getScoreboardManager().updatePlayerOnTab(uhcPlayer);
        }
    }

    @Override
    public void onDisable(){
        if (!Dependencies.getProtocolLibLoaded()){
            return; // Never enabled so don't disable.
        }

        for (UhcPlayer uhcPlayer : getPlayerManager().getAllPlayingPlayers()){
            ProtocolUtils.setPlayerNickName(uhcPlayer, null);
            getScoreboardManager().updatePlayerOnTab(uhcPlayer);
        }
    }

    @EventHandler
    public void onGameStarted(PlayerStartsPlayingEvent e){
        UhcPlayer uhcPlayer = e.getUhcPlayer();

        ProtocolUtils.setPlayerNickName(uhcPlayer, getPlayerNickName(uhcPlayer.getName()));
        getScoreboardManager().updatePlayerOnTab(uhcPlayer);
    }

    /**
     * Make names visible after the game ends.
     */
    @EventHandler
    public void onGameStateChanged(UhcGameStateChangedEvent e) {
        if (e.getNewGameState() != GameState.ENDED) {
            return;
        }

        for (UhcPlayer uhcPlayer : getPlayerManager().getPlayersList()){
            if (uhcPlayer.hasNickName()) {
                ProtocolUtils.setPlayerNickName(uhcPlayer, null);
                getScoreboardManager().updatePlayerOnTab(uhcPlayer);
            }
        }
    }

    @EventHandler
    public void onUhcPlayerStateChange(UhcPlayerStateChangedEvent e){
        if (e.getNewPlayerState() == PlayerState.DEAD){
            UhcPlayer player = e.getPlayer();

            // clear nick
            ProtocolUtils.setPlayerNickName(player, null);
            getScoreboardManager().updatePlayerOnTab(player);
        }
    }

    private String getPlayerNickName(String name){
        if (name.length() > 12){
            name = name.substring(0, 12);
        }

        StringBuilder sb = new StringBuilder();
        sb.append(ChatColor.MAGIC);
        sb.append(name);

        while (sb.length() < 14){
            sb.append("A");
        }

        sb.append(ChatColor.RESET);
        return sb.toString();
    }

}