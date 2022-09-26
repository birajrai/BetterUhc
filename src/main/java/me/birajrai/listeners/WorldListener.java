package me.birajrai.listeners;

import io.papermc.lib.PaperLib;
import me.birajrai.BetterUhc;
import me.birajrai.configuration.MainConfig;
import me.birajrai.game.GameManager;
import me.birajrai.maploader.BiomeTypePopulator;
import me.birajrai.maploader.CaveOresOnlyPopulator;
import me.birajrai.maploader.SurgarCanePopulator;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;

public class WorldListener implements Listener{

    @EventHandler
    public void onWorldInit(WorldInitEvent e){
        World world = e.getWorld();
        GameManager gm = GameManager.getGameManager();
        MainConfig cfg = gm.getConfig();

        String overworldUuid = gm.getMapLoader().getUhcWorldUuid(World.Environment.NORMAL);

        if (world.getName().equals(overworldUuid) && cfg.get(MainConfig.ENABLE_GENERATE_SUGARCANE)){
            world.getPopulators().add(new SurgarCanePopulator(cfg.get(MainConfig.GENERATE_SUGARCANE_PERCENTAGE)));
        }
        if (world.getName().equals(overworldUuid) && cfg.get(MainConfig.REPLACE_OCEAN_BIOMES) && BetterUhc.getVersion() >= 14){
            if (!(PaperLib.isVersion(16) && PaperLib.getMinecraftPatchVersion() > 1)){
                world.getPopulators().add(new BiomeTypePopulator());
            }
        }
        if (world.getName().equals(overworldUuid) && cfg.get(MainConfig.CAVE_ORES_ONLY)){
            world.getPopulators().add(new CaveOresOnlyPopulator());
        }
    }

}