package me.birajrai.threads;

import io.papermc.lib.PaperLib;
import me.birajrai.BetterUhc;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;

import java.util.concurrent.ExecutionException;

public abstract class ChunkLoaderThread implements Runnable {

    private final World world;
    private final int restEveryNumOfChunks, restDuration;

    private final int maxChunk;
    private int x, z;
    private final int totalChunksToLoad;
    private int chunksLoaded;

    public ChunkLoaderThread(World world, int size, int restEveryNumOfChunks, int restDuration) {
        this.world = world;
        this.restEveryNumOfChunks = restEveryNumOfChunks;
        this.restDuration = restDuration;

        maxChunk = Math.round(size/16f) + 1;

        totalChunksToLoad = (2*maxChunk+1)*(2*maxChunk+1);

        x = -maxChunk;
        z = -maxChunk;
    }

    public abstract void onDoneLoadingWorld();
    public abstract void onDoneLoadingChunk(Chunk chunk);

    @Override
    public void run() {
        int loaded = 0;
        while(x <= maxChunk && loaded < restEveryNumOfChunks){
            try {
                Chunk chunk = PaperLib.getChunkAtAsync(world, x, z, true).get();

                if (Bukkit.isPrimaryThread()){
                    onDoneLoadingChunk(chunk);
                }else {
                    Bukkit.getScheduler().runTask(BetterUhc.getPlugin(), () -> onDoneLoadingChunk(chunk));
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            loaded++;
            z++;

            if (z > maxChunk){
                z = -maxChunk;
                x++;
            }
        }

        chunksLoaded += loaded;

        // Cancel world generation if the plugin has been disabled.
        if (!BetterUhc.getPlugin().isEnabled()) {
            Bukkit.getLogger().info("[BetterUHC] Plugin is disabled, stopping world generation!");
            return;
        }

        // Not yet done loading all chunks
        if(x <= maxChunk){
            Bukkit.getLogger().info("[BetterUHC] Loading map "+getLoadingState()+"% - "+chunksLoaded+"/"+totalChunksToLoad+" chunks loaded");

            if (PaperLib.isPaper() && PaperLib.getMinecraftVersion() >= 13){
                Bukkit.getScheduler().scheduleAsyncDelayedTask(BetterUhc.getPlugin(), this, restDuration);
            }else {
                Bukkit.getScheduler().scheduleSyncDelayedTask(BetterUhc.getPlugin(), this, restDuration);
            }
        }
        // Done loading all chunks
        else{
            Bukkit.getScheduler().runTask(BetterUhc.getPlugin(), this::onDoneLoadingWorld);
        }
    }

    public void printSettings(){
        Bukkit.getLogger().info("[BetterUHC] Generating environment "+world.getEnvironment().toString());
        Bukkit.getLogger().info("[BetterUHC] Loading a total "+Math.floor(totalChunksToLoad)+" chunks, up to chunk ( "+maxChunk+" , "+maxChunk+" )");
        Bukkit.getLogger().info("[BetterUHC] Resting "+restDuration+" ticks every "+restEveryNumOfChunks+" chunks");
        Bukkit.getLogger().info("[BetterUHC] Loading map "+getLoadingState()+"%");
    }

    private String getLoadingState(){
        double percentage = 100*(double)chunksLoaded/totalChunksToLoad;
        return world.getEnvironment()+" "+(Math.floor(10*percentage)/10);
    }

}