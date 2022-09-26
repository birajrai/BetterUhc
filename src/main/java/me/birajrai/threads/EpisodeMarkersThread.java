package me.birajrai.threads;

import me.birajrai.BetterUhc;
import me.birajrai.configuration.MainConfig;
import me.birajrai.game.GameManager;
import me.birajrai.languages.Lang;
import me.birajrai.utils.UniversalSound;
import org.bukkit.Bukkit;

public class EpisodeMarkersThread implements Runnable{

    private final GameManager gameManager;
    private final long delay;
    private int episodeNr;

    public EpisodeMarkersThread(GameManager gameManager) {
        this.gameManager = gameManager;
        this.delay = gameManager.getConfig().get(MainConfig.EPISODE_MARKERS_DELAY) * 20;
        this.episodeNr = 0;
    }

    @Override
    public void run() {
        if (episodeNr > 0) {
            gameManager.broadcastInfoMessage(Lang.DISPLAY_EPISODE_MARK.replace("%episode%", episodeNr + ""));
            gameManager.getPlayerManager().playSoundToAll(UniversalSound.FIREWORK_LAUNCH,1,1);
        }
        episodeNr ++;
        gameManager.setEpisodeNumber(episodeNr);
        Bukkit.getScheduler().runTaskLater(BetterUhc.getPlugin(), this, delay);
    }

}