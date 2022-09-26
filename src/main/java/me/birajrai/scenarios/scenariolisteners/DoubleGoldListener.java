package me.birajrai.scenarios.scenariolisteners;

import me.birajrai.customitems.UhcItems;
import me.birajrai.scenarios.Scenario;
import me.birajrai.scenarios.ScenarioListener;
import me.birajrai.utils.OreType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class DoubleGoldListener extends ScenarioListener{

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {

        if (isEnabled(Scenario.CUTCLEAN) || isEnabled(Scenario.TRIPLE_ORES) || isEnabled(Scenario.VEIN_MINER)){
            return;
        }

        Block block = e.getBlock();
        Location loc = e.getBlock().getLocation().add(0.5, 0, 0.5);

        if (OreType.isGold(block.getType())){
            block.setType(Material.AIR);
            loc.getWorld().dropItem(loc,new ItemStack(Material.GOLD_INGOT, 2));
            UhcItems.spawnExtraXp(loc,6);
        }
    }

}