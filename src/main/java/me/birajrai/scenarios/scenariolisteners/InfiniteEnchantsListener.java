package me.birajrai.scenarios.scenariolisteners;

import me.birajrai.events.UhcStartedEvent;
import me.birajrai.exceptions.UhcPlayerNotOnlineException;
import me.birajrai.players.UhcPlayer;
import me.birajrai.scenarios.ScenarioListener;
import me.birajrai.utils.UniversalMaterial;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

public class InfiniteEnchantsListener extends ScenarioListener{

    @EventHandler
    public void onGameStarted(UhcStartedEvent e){
        ItemStack enchantingTables = UniversalMaterial.ENCHANTING_TABLE.getStack(64);
        ItemStack anvils = new ItemStack(Material.ANVIL, 64);
        ItemStack lapisBlocks = new ItemStack(Material.LAPIS_BLOCK, 64);

        for (UhcPlayer uhcPlayer : e.getPlayerManager().getOnlinePlayingPlayers()){
            try {
                Player player = uhcPlayer.getPlayer();
                player.getInventory().addItem(enchantingTables, anvils, lapisBlocks);
                player.setLevel(Integer.MAX_VALUE);
            }catch (UhcPlayerNotOnlineException ex){
                // No rod for offline players
            }
        }
    }

}