package me.birajrai.maploader;

import me.birajrai.BetterUhc;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;

import java.util.Random;

public class BiomeTypePopulator extends BlockPopulator{

    @Override
    public void populate(World world, Random random, Chunk chunk){
        for (int x = 1; x < 15; x++) {
            for (int z = 1; z < 15; z++) {

                Block block = chunk.getBlock(x, 1, z);
                Biome replacementBiome = getReplacementBiome(block.getBiome());

                if (BetterUhc.getVersion() < 16){
                    if (replacementBiome != null) {
                        block.setBiome(replacementBiome);
                    }
                }else {
                    for (int y = 0; y < 200; y++) {
                        block = chunk.getBlock(x, y, z);

                        if (replacementBiome != null) {
                            block.setBiome(replacementBiome);
                        }
                    }
                }
            }
        }
    }

    private Biome getReplacementBiome(Biome biome) {
        if (biome.toString().contains("OCEAN")) {
            return Biome.FOREST;
        }

        return null;
    }

}