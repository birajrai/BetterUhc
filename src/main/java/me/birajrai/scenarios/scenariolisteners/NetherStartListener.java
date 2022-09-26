package me.birajrai.scenarios.scenariolisteners;

import me.birajrai.configuration.MainConfig;
import me.birajrai.events.UhcPreTeleportEvent;
import me.birajrai.players.UhcTeam;
import me.birajrai.scenarios.Scenario;
import me.birajrai.scenarios.ScenarioListener;
import me.birajrai.utils.LocationUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;

public class NetherStartListener extends ScenarioListener{

    @Override
    public void onEnable(){
        if (!getConfiguration().get(MainConfig.ENABLE_NETHER)){
            Bukkit.broadcastMessage(ChatColor.RED + "[UhcCore] For NetherStart the nether needs to be enabled first!");
            getScenarioManager().disableScenario(Scenario.NETHER_START);
        }
    }

    @EventHandler
    public void onPreTeleport(UhcPreTeleportEvent e){
        World nether = getGameManager().getMapLoader().getUhcWorld(World.Environment.NETHER);
        double maxDistance = 0.9 * (nether.getWorldBorder().getSize()/2);

        for(UhcTeam team : getPlayerManager().listUhcTeams()){
            Location newLoc = LocationUtils.findRandomSafeLocation(nether, maxDistance);
            team.setStartingLocation(newLoc);
        }
    }

}