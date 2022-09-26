package me.birajrai.scenarios.scenariolisteners;

import me.birajrai.languages.Lang;
import me.birajrai.scenarios.ScenarioListener;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.spigotmc.event.entity.EntityMountEvent;

public class HorselessListener extends ScenarioListener{

    @EventHandler
    public void onHorseRide(EntityMountEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = ((Player) e.getEntity()).getPlayer();

            if (e.getMount().getType().equals(EntityType.HORSE)) {
                p.sendMessage(Lang.SCENARIO_HORSELESS_ERROR);
                e.setCancelled(true);
            }
        }
    }

}