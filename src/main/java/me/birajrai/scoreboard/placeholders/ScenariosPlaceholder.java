package me.birajrai.scoreboard.placeholders;

import me.birajrai.game.GameManager;
import me.birajrai.players.UhcPlayer;
import me.birajrai.scenarios.Scenario;
import me.birajrai.scenarios.ScenarioManager;
import me.birajrai.scoreboard.Placeholder;
import me.birajrai.scoreboard.ScoreboardType;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ScenariosPlaceholder extends Placeholder {

    private final Map<UUID, Integer> lastShownScenario;

    public ScenariosPlaceholder(){
        super("scenarios");
        lastShownScenario = new HashMap<>();
    }

    @Override
    public String getReplacement(UhcPlayer uhcPlayer, Player player, ScoreboardType scoreboardType, String placeholder){
        ScenarioManager scenarioManager = GameManager.getGameManager().getScenarioManager();

        if (scenarioManager.getEnabledScenarios().isEmpty()){
            return "-";
        }

        Scenario[] activeScenarios = scenarioManager.getEnabledScenarios().toArray(new Scenario[0]);

        int showScenario = lastShownScenario.getOrDefault(player.getUniqueId(), -1) + 1;
        if (showScenario >= activeScenarios.length){
            showScenario = 0;
        }
        lastShownScenario.put(player.getUniqueId(), showScenario);
        return activeScenarios[showScenario].getInfo().getName();
    }

}