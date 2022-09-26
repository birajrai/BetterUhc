package me.birajrai.scenarios.scenariolisteners;

import me.birajrai.players.UhcTeam;
import me.birajrai.scenarios.Option;
import me.birajrai.scenarios.ScenarioListener;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

public class TeamInventoryListener extends ScenarioListener{

    @Option(key = "drop-on-team-elimination")
    private boolean dropOnLastDeath = false;

    public void dropTeamInventory(UhcTeam team, Location location)
    {
        if (dropOnLastDeath)
        {
            World world = location.getWorld();
            for (ItemStack stack : team.getTeamInventory().getContents())
            {
                if (stack != null)
                {
                    world.dropItemNaturally(location, stack);
                }
            }
            team.getTeamInventory().clear();
        }
    }
}
