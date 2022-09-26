package me.birajrai.scenarios.scenariolisteners;

import me.birajrai.scenarios.ScenarioListener;
import me.birajrai.utils.OreType;
import me.birajrai.utils.UniversalSound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;

public class BloodDiamondsListener extends ScenarioListener{

    @EventHandler (ignoreCancelled = true, priority = EventPriority.LOW)
    public void onBlockBreak(BlockBreakEvent e){
        if (!OreType.DIAMOND.equals(e.getBlock().getType())){
            return;
        }

        Player p = e.getPlayer();
        p.getWorld().playSound(p.getLocation(), UniversalSound.PLAYER_HURT.getSound(), 1, 1);

        if (p.getHealth() < 1){
            p.setHealth(0);
        }else {
            p.setHealth(p.getHealth() - 1);
        }
    }

}