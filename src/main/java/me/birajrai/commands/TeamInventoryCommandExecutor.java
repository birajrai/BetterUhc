package me.birajrai.commands;

import me.birajrai.exceptions.UhcPlayerDoesNotExistException;
import me.birajrai.languages.Lang;
import me.birajrai.players.PlayerManager;
import me.birajrai.players.PlayerState;
import me.birajrai.players.UhcPlayer;
import me.birajrai.scenarios.Scenario;
import me.birajrai.scenarios.ScenarioManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeamInventoryCommandExecutor implements CommandExecutor{

    private final PlayerManager playerManager;
    private final ScenarioManager scenarioManager;

    public TeamInventoryCommandExecutor(PlayerManager playerManager, ScenarioManager scenarioManager){
        this.playerManager = playerManager;
        this.scenarioManager = scenarioManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)){
            sender.sendMessage("Only players can use this command!");
            return true;
        }
        Player player = (Player) sender;

        if (!scenarioManager.isEnabled(Scenario.TEAM_INVENTORY)){
            player.sendMessage(Lang.SCENARIO_TEAMINVENTORY_DISABLED);
            return true;
        }

        UhcPlayer uhcPlayer = playerManager.getUhcPlayer(player);

        if (args.length == 1 && player.hasPermission("scenarios.teaminventory.other")){
            try {
                uhcPlayer = playerManager.getUhcPlayer(args[0]);
            }catch (UhcPlayerDoesNotExistException ex){
                player.sendMessage(ChatColor.RED + "That player cannot be found!");
                return true;
            }

            if (uhcPlayer.getState() != PlayerState.PLAYING){
                player.sendMessage(ChatColor.RED + "That player is currently not playing!");
                return true;
            }
        }

        if (uhcPlayer.getState() != PlayerState.PLAYING){
            player.sendMessage(Lang.SCENARIO_TEAMINVENTORY_ERROR);
            return true;
        }

        player.sendMessage(Lang.SCENARIO_TEAMINVENTORY_OPEN);
        player.openInventory(uhcPlayer.getTeam().getTeamInventory());
        return true;
    }

}