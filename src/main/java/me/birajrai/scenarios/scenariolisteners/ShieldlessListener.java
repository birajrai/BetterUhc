package me.birajrai.scenarios.scenariolisteners;

import me.birajrai.languages.Lang;
import me.birajrai.scenarios.ScenarioListener;
import me.birajrai.utils.UniversalMaterial;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

public class ShieldlessListener extends ScenarioListener {

    @EventHandler
    public void onCraftItem(CraftItemEvent e) {
        ItemStack item = e.getCurrentItem();

        if (item.getType() != Material.AIR && item.getType() == UniversalMaterial.SHIELD.getType()){
            e.getWhoClicked().sendMessage(Lang.SCENARIO_SHIELDLESS_ERROR);
            e.setCancelled(true);
        }
    }

}