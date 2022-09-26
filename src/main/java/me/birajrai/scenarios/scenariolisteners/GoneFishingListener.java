package me.birajrai.scenarios.scenariolisteners;

import me.birajrai.events.UhcStartedEvent;
import me.birajrai.exceptions.UhcPlayerNotOnlineException;
import me.birajrai.players.UhcPlayer;
import me.birajrai.scenarios.Option;
import me.birajrai.scenarios.ScenarioListener;
import me.birajrai.utils.VersionUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GoneFishingListener extends ScenarioListener{

    @Option(key = "lure-enchantment-level")
    private int lureLevel = 3;
    @Option(key = "luck-enchantment-level")
    private int luckLevel = 3;
    @Option(key = "infinite-enchants-items")
    private boolean infiniteEnchantsItems = false;

    @EventHandler
    public void onGameStarted(UhcStartedEvent e){
        ItemStack rod = new ItemStack(Material.FISHING_ROD);
        rod.addUnsafeEnchantment(Enchantment.LURE, lureLevel);
        rod.addUnsafeEnchantment(Enchantment.LUCK, luckLevel);

        ItemMeta meta = rod.getItemMeta();
        VersionUtils.getVersionUtils().setItemUnbreakable(meta, true);
        rod.setItemMeta(meta);

        ItemStack anvils = new ItemStack(Material.ANVIL, 64);

        for (UhcPlayer uhcPlayer : e.getPlayerManager().getOnlinePlayingPlayers()){
            try {
                // Give the rod
                uhcPlayer.getPlayer().getInventory().addItem(rod);

                if (infiniteEnchantsItems) {
                    // Give player 10000 xp levels
                    uhcPlayer.getPlayer().setLevel(10000);
                    // Give player 64 anvils
                    uhcPlayer.getPlayer().getInventory().addItem(anvils);
                }
            }catch (UhcPlayerNotOnlineException ex){
                // No rod for offline players
            }
        }
    }

}