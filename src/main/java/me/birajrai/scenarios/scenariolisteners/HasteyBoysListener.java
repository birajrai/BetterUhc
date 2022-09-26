package me.birajrai.scenarios.scenariolisteners;

import me.birajrai.customitems.CraftsManager;
import me.birajrai.scenarios.Option;
import me.birajrai.scenarios.ScenarioListener;
import me.birajrai.utils.UniversalMaterial;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

public class HasteyBoysListener extends ScenarioListener{

    @Option(key = "efficiency")
    private int efficiency = 3;
    
    @Option(key = "durability")
    private int durability = 1;

    @Option(key = "stone-tools")
    private boolean stone_tools = false;

    @EventHandler
    public void onPlayerCraft(CraftItemEvent e){
        ItemStack item = e.getCurrentItem();

        // Don't apply hastey boy effects to custom crafted items.
        if (CraftsManager.isCraftItem(item)){
            return;
        }

        if (stone_tools){
            if (item.getType() == UniversalMaterial.WOODEN_SWORD.getType()){
                item.setType(Material.STONE_SWORD);
            }else if (item.getType() == UniversalMaterial.WOODEN_PICKAXE.getType()){
                item.setType(Material.STONE_PICKAXE);
            }else if (item.getType() == UniversalMaterial.WOODEN_SHOVEL.getType()){
                item.setType(UniversalMaterial.STONE_SHOVEL.getType());
            }else if (item.getType() == UniversalMaterial.WOODEN_AXE.getType()){
                item.setType(Material.STONE_AXE);
            }else if (item.getType() == UniversalMaterial.WOODEN_HOE.getType()){
                item.setType(Material.STONE_HOE);
            }
        }

        try {
            item.addEnchantment(Enchantment.DIG_SPEED,efficiency);
            item.addEnchantment(Enchantment.DURABILITY,durability);
        }catch (IllegalArgumentException ex){
            // Nothing
        }

    }

}