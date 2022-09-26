package me.birajrai.scenarios.scenariolisteners;

import me.birajrai.scenarios.ScenarioListener;
import me.birajrai.utils.OreType;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;

public class GoldLessListener extends ScenarioListener{

    @EventHandler (priority = EventPriority.LOW)
    public void onBlockBreak(BlockBreakEvent e){
        if (OreType.isGold(e.getBlock().getType())){
            e.getBlock().setType(Material.AIR);
        }

    }

}