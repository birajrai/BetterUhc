package me.birajrai.scenarios.scenariolisteners;

import me.birajrai.languages.Lang;
import me.birajrai.scenarios.ScenarioListener;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

public class RodlessListener extends ScenarioListener {

    @EventHandler
    public void onCraftItem(CraftItemEvent e) {
        ItemStack item = e.getCurrentItem();

        if (item.getType().equals(Material.FISHING_ROD)) {
            e.getWhoClicked().sendMessage(Lang.SCENARIO_RODLESS_ERROR);
            e.setCancelled(true);
        }
    }

}