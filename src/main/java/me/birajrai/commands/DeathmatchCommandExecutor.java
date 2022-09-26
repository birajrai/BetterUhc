package me.birajrai.commands;

import me.birajrai.BetterUhc;
import me.birajrai.configuration.MainConfig;
import me.birajrai.game.GameManager;
import me.birajrai.game.GameState;
import me.birajrai.game.handlers.DeathmatchHandler;
import me.birajrai.threads.TimeBeforeDeathmatchThread;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class DeathmatchCommandExecutor implements CommandExecutor{

    private final DeathmatchHandler deathmatchHandler;
    private final GameManager gameManager;

    public DeathmatchCommandExecutor(GameManager gameManager, DeathmatchHandler deathmatchHandler){
        this.gameManager = gameManager;
        this.deathmatchHandler = deathmatchHandler;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1 || !args[0].equalsIgnoreCase("start")){
            sender.sendMessage(ChatColor.RED + "Usage: /deathmatch start");
            return true;
        }

        // Check if in game.
        if (gameManager.getGameState() != GameState.PLAYING){
            sender.sendMessage(ChatColor.RED + "You may only use this command during the game.");
            return true;
        }

        // Start TimeBeforeEndThread if time limit was not enabled.
        if (gameManager.getRemainingTime() == 0 && !gameManager.getConfig().get(MainConfig.ENABLE_DEATHMATCH)){
            gameManager.setRemainingTime(10);
            Bukkit.getScheduler().runTaskAsynchronously(BetterUhc.getPlugin(), new TimeBeforeDeathmatchThread(gameManager, deathmatchHandler));
            sender.sendMessage(ChatColor.GREEN + "Starting deathmatch!");
            return true;
        }

        // If deathmatch is about to start don't allow the command.
        if (gameManager.getRemainingTime() < 10){
            sender.sendMessage(ChatColor.RED + "Deathmatch is already starting!");
            return true;
        }

        gameManager.setRemainingTime(10);
        sender.sendMessage(ChatColor.GREEN + "Starting deathmatch!");
        return true;
    }

}