package me.birajrai.scenarios;

import me.birajrai.configuration.MainConfig;
import me.birajrai.game.GameManager;
import me.birajrai.players.PlayerManager;
import me.birajrai.players.TeamManager;
import me.birajrai.scoreboard.ScoreboardManager;
import org.bukkit.event.Listener;

/**
 * Used as event listener for a scenario (automatically registered).
 * In here you should handle anything related to the scenario.
 * See {@link #onEnable()} & {@link #onDisable()}
 */
public abstract class ScenarioListener implements Listener {

    public GameManager getGameManager(){
        return GameManager.getGameManager();
    }

    public PlayerManager getPlayerManager(){
        return getGameManager().getPlayerManager();
    }

    public TeamManager getTeamManager(){
        return getGameManager().getTeamManager();
    }

    public ScoreboardManager getScoreboardManager(){
        return getGameManager().getScoreboardManager();
    }

    public ScenarioManager getScenarioManager(){
        return getGameManager().getScenarioManager();
    }

    public MainConfig getConfiguration(){
        return getGameManager().getConfig();
    }

    /**
     * Used to check if a scenario is enabled.
     * @param scenario Scenario to check.
     * @return Returns true if the scenario is enabled.
     */
    public boolean isEnabled(Scenario scenario) {
        return getScenarioManager().isEnabled(scenario);
    }

    /**
     * Gets called when the scenario is enabled.
     */
    public void onEnable() {}

    /**
     * Gets called when the scenario is disabled.
     */
    public void onDisable() {}

}