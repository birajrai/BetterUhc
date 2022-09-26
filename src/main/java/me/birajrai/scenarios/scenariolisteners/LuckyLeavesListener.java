package me.birajrai.scenarios.scenariolisteners;

import me.birajrai.scenarios.ScenarioListener;
import me.birajrai.utils.RandomUtils;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.inventory.ItemStack;

public class LuckyLeavesListener extends ScenarioListener{

    @EventHandler (ignoreCancelled = true)
    public void onLeaveDecay(LeavesDecayEvent e){
        int random = RandomUtils.randomInteger(0, 200);

        if (random > 1){
            return;
        }

        // add gapple
        e.getBlock().getWorld().dropItem(e.getBlock().getLocation().add(.5,0,.5),new ItemStack(Material.GOLDEN_APPLE));
    }

}