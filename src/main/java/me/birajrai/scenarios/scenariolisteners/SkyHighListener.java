package me.birajrai.scenarios.scenariolisteners;

import me.birajrai.BetterUhc;
import me.birajrai.events.UhcStartedEvent;
import me.birajrai.exceptions.UhcPlayerNotOnlineException;
import me.birajrai.game.GameManager;
import me.birajrai.game.GameState;
import me.birajrai.languages.Lang;
import me.birajrai.players.PlayerState;
import me.birajrai.players.UhcPlayer;
import me.birajrai.scenarios.Option;
import me.birajrai.scenarios.ScenarioListener;
import me.birajrai.utils.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class SkyHighListener extends ScenarioListener{

    private int taskId;
    @Option(key = "time-before-start")
    private long delay = 60*30;
    @Option(key = "time-between-damage")
    private long period = 30;
    @Option(key = "y-layer")
    private int yLayer = 120;

    public SkyHighListener(){
        taskId = -1;
    }

    @EventHandler
    public void onGameStarted(UhcStartedEvent e){
        taskId = Bukkit.getScheduler().scheduleSyncDelayedTask(BetterUhc.getPlugin(), new SkyHighThread(this), delay*TimeUtils.SECOND_TICKS);
    }

    @Override
    public void onEnable() {
        // start thread
        if (getGameManager().getGameState() == GameState.PLAYING ||
                getGameManager().getGameState() == GameState.DEATHMATCH){
            long timeUntilFirstRun = delay - GameManager.getGameManager().getElapsedTime();
            if (timeUntilFirstRun < 0){
                timeUntilFirstRun = 0;
            }
            taskId = Bukkit.getScheduler().scheduleSyncDelayedTask(BetterUhc.getPlugin(), new SkyHighThread(this), timeUntilFirstRun*TimeUtils.SECOND_TICKS);
        }
    }

    @Override
    public void onDisable() {
        // stop thread
        if (taskId != -1) {
            Bukkit.getScheduler().cancelTask(taskId);
        }
    }

    public static class SkyHighThread implements Runnable{

        private final SkyHighListener listener;

        public SkyHighThread(SkyHighListener listener){
            this.listener = listener;
        }

        @Override
        public void run() {
            // damage players
            for (UhcPlayer uhcPlayer : GameManager.getGameManager().getPlayerManager().getOnlinePlayingPlayers()){
                if (uhcPlayer.getState() == PlayerState.PLAYING) {
                    try {
                        Player player = uhcPlayer.getPlayer();
                        if (player.getLocation().getBlockY() < listener.yLayer) {
                            player.sendMessage(Lang.SCENARIO_SKYHIGH_DAMAGE);
                            player.setHealth(player.getHealth() - 1);
                        }
                    } catch (UhcPlayerNotOnlineException ex) {
                        // No los of hp for offline players.
                    }
                }
            }
            listener.taskId = Bukkit.getScheduler().scheduleSyncDelayedTask(BetterUhc.getPlugin(), this, listener.period*TimeUtils.SECOND_TICKS);
        }

    }

}