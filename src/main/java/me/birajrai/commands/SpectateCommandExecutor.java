package me.birajrai.commands;

import me.birajrai.customitems.UhcItems;
import me.birajrai.exceptions.UhcTeamException;
import me.birajrai.game.GameManager;
import me.birajrai.game.GameState;
import me.birajrai.game.handlers.ScoreboardHandler;
import me.birajrai.languages.Lang;
import me.birajrai.players.PlayerState;
import me.birajrai.players.UhcPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpectateCommandExecutor implements CommandExecutor{

    private final GameManager gameManager;
    private final ScoreboardHandler scoreboardHandler;

    public SpectateCommandExecutor(GameManager gameManager, ScoreboardHandler scoreboardHandler){
        this.gameManager = gameManager;
        this.scoreboardHandler = scoreboardHandler;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)){
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        Player player = (Player) sender;

        if (gameManager.getGameState() != GameState.WAITING){
            player.sendMessage(Lang.COMMAND_SPECTATE_ERROR);
            return true;
        }

        UhcPlayer uhcPlayer = gameManager.getPlayerManager().getUhcPlayer(player);

        if (uhcPlayer.getState() == PlayerState.DEAD){
            setPlayerPlaying(player, uhcPlayer);
            player.sendMessage(Lang.COMMAND_SPECTATE_PLAYING);
            return true;
        }

        setPlayerSpectating(player, uhcPlayer);
        player.sendMessage(Lang.COMMAND_SPECTATE_SPECTATING);
        return true;
    }

    private void setPlayerSpectating(Player player, UhcPlayer uhcPlayer){
        uhcPlayer.setState(PlayerState.DEAD);

        // Clear lobby items
        player.getInventory().clear();

        if (!uhcPlayer.getTeam().isSolo()){
            try {
                uhcPlayer.getTeam().leave(uhcPlayer);
            }catch (UhcTeamException ex){
                ex.printStackTrace();
            }
        }

        scoreboardHandler.updatePlayerOnTab(uhcPlayer);
    }

    private void setPlayerPlaying(Player player, UhcPlayer uhcPlayer){
        uhcPlayer.setState(PlayerState.WAITING);
        scoreboardHandler.updatePlayerOnTab(uhcPlayer);

        // Give lobby items back
        UhcItems.giveLobbyItemsTo(player);
    }

}