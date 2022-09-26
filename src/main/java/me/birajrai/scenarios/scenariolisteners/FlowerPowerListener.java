package me.birajrai.scenarios.scenariolisteners;

import me.birajrai.BetterUhc;
import me.birajrai.configuration.YamlFile;
import me.birajrai.customitems.UhcItems;
import me.birajrai.scenarios.ScenarioListener;
import me.birajrai.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class FlowerPowerListener extends ScenarioListener{

    private static final UniversalMaterial[] FLOWERS = new UniversalMaterial[]{
            UniversalMaterial.POPPY,
            UniversalMaterial.BLUE_ORCHID,
            UniversalMaterial.ALLIUM,
            UniversalMaterial.AZURE_BLUET,
            UniversalMaterial.RED_TULIP,
            UniversalMaterial.ORANGE_TULIP,
            UniversalMaterial.WHITE_TULIP,
            UniversalMaterial.PINK_TULIP,
            UniversalMaterial.OXEYE_DAISY,
            UniversalMaterial.SUNFLOWER,
            UniversalMaterial.LILAC,
            UniversalMaterial.ROSE_BUSH,
            UniversalMaterial.PEONY,
            UniversalMaterial.DEAD_BUSH,
            UniversalMaterial.DANDELION
    };

    private List<JsonItemStack> flowerDrops;
    private int expPerFlower;

    @Override
    public void onEnable(){
        flowerDrops = new ArrayList<>();

        String source = BetterUhc.getVersion() < 13 ? "flowerpower-1.8.yml" : "flowerpower-1.13.yml";
        YamlFile cfg;

        try{
            cfg = FileUtils.saveResourceIfNotAvailable(BetterUhc.getPlugin(), "flowerpower.yml", source);
        }catch (InvalidConfigurationException ex){
            ex.printStackTrace();
            return;
        }

        expPerFlower = cfg.getInt("exp-per-flower", 2);

        for (String drop : cfg.getStringList("drops")){
            try {
                JsonItemStack flowerDrop = JsonItemUtils.getItemFromJson(drop);
                flowerDrops.add(flowerDrop);
            }catch (Exception ex){
                Bukkit.getLogger().severe("[UhcCore] Failed to parse FlowerPower item: "+drop+"!");
                Bukkit.getLogger().severe(ex.getMessage());
            }
        }
    }

    @EventHandler (priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent e){
        Block block = e.getBlock();

        // For tall flowers start with the bottom block.
        Block below = block.getRelative(BlockFace.DOWN);
        if (isFlower(below)){
            block = below;
        }

        if (isFlower(block)){
            Location blockLoc = block.getLocation().add(.5,.5,.5);
            block.setType(Material.AIR);
            UhcItems.spawnExtraXp(blockLoc, expPerFlower);

            int random = RandomUtils.randomInteger(0, flowerDrops.size()-1);
            ItemStack drop = flowerDrops.get(random);
            blockLoc.getWorld().dropItem(blockLoc, drop);
        }
    }

    private boolean isFlower(Block block){
        for (UniversalMaterial flower : FLOWERS){
            if (flower.equals(block)) return true;
        }

        if (BetterUhc.getVersion() >= 14){
            String material = block.getType().toString();
            return material.equals("LILY_OF_THE_VALLEY") || material.equals("CORNFLOWER");
        }
        return false;
    }

}