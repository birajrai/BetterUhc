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

import java.util.Optional;

public class DoubleOresListener extends ScenarioListener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {

        if (isEnabled(Scenario.VEIN_MINER)) {
            return;
        }

        Block block = e.getBlock();
        Location loc = e.getBlock().getLocation().add(0.5, 0, 0.5);
        Material type = block.getType();
        ItemStack drop = null;

        Optional<OreType> oreType = OreType.valueOf(type);

        if (oreType.isPresent()) {
            int xp = oreType.get().getXpPerBlock() * 2;
            int count = 2;

            if (oreType.get() == OreType.GOLD && isEnabled(Scenario.DOUBLE_GOLD)) {
                count *= 2;
            }

            drop = new ItemStack(oreType.get().getDrop(), count);
            UhcItems.spawnExtraXp(loc,xp);
        }

        if (type == Material.SAND) {
            drop = new ItemStack(Material.GLASS);
        } else if (type == Material.GRAVEL) {
            drop = new ItemStack(Material.FLINT);
        }

        if (drop != null) {
            block.setType(Material.AIR);
            loc.getWorld().dropItem(loc, drop);
        }
    }

}