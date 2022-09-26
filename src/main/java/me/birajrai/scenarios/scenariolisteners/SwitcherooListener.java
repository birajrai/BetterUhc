package me.birajrai.scenarios.scenariolisteners;

import me.birajrai.languages.Lang;
import me.birajrai.scenarios.ScenarioListener;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class SwitcherooListener extends ScenarioListener{

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent e){
        if (!(e.getEntity() instanceof Player)){
            return;
        }

        Player player = ((Player) e.getEntity()).getPlayer();

        if (!(e.getDamager() instanceof Arrow)){
            return;
        }

        Arrow arrow = (Arrow) e.getDamager();

        if (!(arrow.getShooter() instanceof  Player)){
            return;
        }

        Player shooter = ((Player) arrow.getShooter()).getPlayer();

        if (player.equals(shooter)){
            return;
        }

        arrow.remove();

        Location playerLoc = player.getLocation();
        Location shooterLoc = shooter.getLocation();

        player.teleport(shooterLoc);
        shooter.teleport(playerLoc);

        player.sendMessage(Lang.SCENARIO_SWITCHEROO_SWITCH.replace("%player%", shooter.getName()));
        shooter.sendMessage(Lang.SCENARIO_SWITCHEROO_SWITCH.replace("%player%", player.getName()));
    }

}