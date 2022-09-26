package me.birajrai.scenarios.scenariolisteners;

import me.birajrai.events.PlayerStartsPlayingEvent;
import me.birajrai.exceptions.UhcPlayerNotOnlineException;
import me.birajrai.scenarios.ScenarioListener;
import me.birajrai.utils.UniversalMaterial;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class NineSlotsListener extends ScenarioListener{

    private ItemStack fillItem;

    @Override
    public void onEnable(){
        fillItem = UniversalMaterial.LIGHT_GRAY_STAINED_GLASS_PANE.getStack();
        ItemMeta meta = fillItem.getItemMeta();
        meta.setDisplayName(ChatColor.RED.toString() + ChatColor.BOLD + "BLOCKED");
        fillItem.setItemMeta(meta);
    }

    @EventHandler
    public void onGameStarted(PlayerStartsPlayingEvent e){
        try{
            fillInventory(e.getUhcPlayer().getPlayer());
        }catch (UhcPlayerNotOnlineException ex){
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){
        ItemStack item = e.getCurrentItem();

        // Only handle clicked items.
        if (item == null){
            return;
        }

        if (item.equals(fillItem)){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e){
        List<ItemStack> drops = e.getDrops();

        // Remove all fill items.
        while (drops.remove(fillItem)){}
    }

    private void fillInventory(Player player){
        for (int i = 9; i <= 35; i++) {
            player.getInventory().setItem(i, fillItem);
        }
    }

}