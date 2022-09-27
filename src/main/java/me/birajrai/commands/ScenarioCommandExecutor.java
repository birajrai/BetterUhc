package me.birajrai.commands;

import me.birajrai.scenarios.ScenarioManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ScenarioCommandExecutor implements CommandExecutor{

    private final ScenarioManager scenarioManager;

    public ScenarioCommandExecutor(ScenarioManager scenarioManager){
        this.scenarioManager = scenarioManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (sender instanceof Player){
            Player p = ((Player) sender).getPlayer();
            // get inventory
            p.openInventory(scenarioManager.getScenarioMainInventory(p.hasPermission("betteruhc.scenarios.edit")));
        }else {
            sender.sendMessage("Only players can use this command.");
        }
        return true;
    }

}