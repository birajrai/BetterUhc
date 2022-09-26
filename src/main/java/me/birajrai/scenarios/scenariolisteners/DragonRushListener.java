package me.birajrai.scenarios.scenariolisteners;

import me.birajrai.configuration.MainConfig;
import me.birajrai.exceptions.UhcPlayerNotOnlineException;
import me.birajrai.game.GameManager;
import me.birajrai.players.PlayerState;
import me.birajrai.players.UhcPlayer;
import me.birajrai.scenarios.Scenario;
import me.birajrai.scenarios.ScenarioListener;
import me.birajrai.utils.RandomUtils;
import me.birajrai.utils.UniversalMaterial;
import me.birajrai.utils.VersionUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.ArrayList;
import java.util.List;

public class DragonRushListener extends ScenarioListener{

    private final List<Block> portalBlocks;

    public DragonRushListener(){
        portalBlocks = new ArrayList<>();
    }

    @Override
    public void onEnable(){
        if (!GameManager.getGameManager().getConfig().get(MainConfig.ENABLE_THE_END)){
            Bukkit.broadcastMessage(ChatColor.RED + "[UhcCore] For DragonRush the end needs to be enabled first!");
            getScenarioManager().disableScenario(Scenario.DRAGON_RUSH);
            return;
        }

        Location portalLoc = getPortalLocation();

        portalBlocks.add(portalLoc.clone().add(1, 0, 2).getBlock());
        portalBlocks.add(portalLoc.clone().add(0, 0, 2).getBlock());
        portalBlocks.add(portalLoc.clone().add(-1, 0, 2).getBlock());

        portalBlocks.add(portalLoc.clone().add(-2, 0, 1).getBlock());
        portalBlocks.add(portalLoc.clone().add(-2, 0, 0).getBlock());
        portalBlocks.add(portalLoc.clone().add(-2, 0, -1).getBlock());

        portalBlocks.add(portalLoc.clone().add(1, 0, -2).getBlock());
        portalBlocks.add(portalLoc.clone().add(0, 0, -2).getBlock());
        portalBlocks.add(portalLoc.clone().add(-1, 0, -2).getBlock());

        portalBlocks.add(portalLoc.clone().add(2, 0, 1).getBlock());
        portalBlocks.add(portalLoc.clone().add(2, 0, 0).getBlock());
        portalBlocks.add(portalLoc.clone().add(2, 0, -1).getBlock());

        int i = 0;
        BlockFace blockFace = BlockFace.NORTH;
        for (Block block : portalBlocks){
            block.setType(UniversalMaterial.END_PORTAL_FRAME.getType());
            VersionUtils.getVersionUtils().setEndPortalFrameOrientation(block, blockFace);
            if (RandomUtils.randomInteger(0, 2) == 1){
                VersionUtils.getVersionUtils().setEye(block, true);
            }
            i++;
            if (i == 3){
                i = 0;
                if (blockFace == BlockFace.NORTH){
                    blockFace = BlockFace.EAST;
                }else if (blockFace == BlockFace.EAST){
                    blockFace = BlockFace.SOUTH;
                }else if (blockFace == BlockFace.SOUTH){
                    blockFace = BlockFace.WEST;
                }
            }
        }
    }

    @Override
    public void onDisable() {
        for (Block block : portalBlocks){
            block.setType(Material.AIR);
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e){
        if (e.getEntityType() != EntityType.ENDER_DRAGON){
            return;
        }

        if (e.getEntity().getKiller() == null) {
            return;
        }

        Player killer = e.getEntity().getKiller();
        UhcPlayer uhcKiller = getPlayerManager().getUhcPlayer(killer);

        List<UhcPlayer> spectators = new ArrayList<>();

        for (UhcPlayer playingPlayer : getPlayerManager().getAllPlayingPlayers()){

            if (!playingPlayer.isInTeamWith(uhcKiller)){
                spectators.add(playingPlayer);
            }
        }

        for (UhcPlayer spectator : spectators){
            spectator.setState(PlayerState.DEAD);

            try {
                Player all = spectator.getPlayer();
                all.setGameMode(GameMode.SPECTATOR);
                all.teleport(killer);
            }catch (UhcPlayerNotOnlineException exeption){
                // Nothing
            }
        }

        getPlayerManager().checkIfRemainingPlayers();
    }

    private Location getPortalLocation(){
        World world = getGameManager().getMapLoader().getUhcWorld(World.Environment.NORMAL);
        int portalY = 0;

        for (int x = -4; x < 4; x++) {
            for (int z = -4; z < 4; z++) {
                int y = getHighestBlock(world, x, z);
                if (y > portalY){
                    portalY = y;
                }
            }
        }

        return new Location(world, 0, portalY+1, 0);
    }

    private int getHighestBlock(World world, int x, int z){
        int y = 150;
        while (world.getBlockAt(x, y, z).getType() == Material.AIR){
            y--;
        }

        return y;
    }

}