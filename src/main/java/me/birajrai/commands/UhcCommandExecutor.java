package me.birajrai.commands;

import me.birajrai.BetterUhc;
import me.birajrai.game.GameManager;
import me.birajrai.game.GameState;
import me.birajrai.players.PlayerManager;
import me.birajrai.players.PlayerState;
import me.birajrai.players.UhcPlayer;
import me.birajrai.players.UhcTeam;
import me.birajrai.threads.PreStartThread;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UhcCommandExecutor implements CommandExecutor{

	private final GameManager gameManager;

	public UhcCommandExecutor(GameManager gameManager){
		this.gameManager = gameManager;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){

		if (args.length == 1 && args[0].equalsIgnoreCase("version")){
			sender.sendMessage(ChatColor.GREEN + "BetterUhc version: " + BetterUhc.getPlugin().getDescription().getVersion());
			return true;
		}

		if (args.length == 1 && args[0].equalsIgnoreCase("reload")){
			if (!sender.hasPermission("betteruhc.commands.reload")){
				sender.sendMessage(ChatColor.RED + "You don't have the permission to use this command");
				return true;
			}

			gameManager.getScoreboardManager().getScoreboardLayout().loadFile();
			Bukkit.getServer().resetRecipes();
			gameManager.loadConfig();
			sender.sendMessage(ChatColor.GREEN + "config.yml, lang.yml and scoreboard.yml have been reloaded");
			return true;
		}

		if (args.length == 1 && args[0].equalsIgnoreCase("update")){
			if (!sender.hasPermission("betteruhc.commands.update")){
				sender.sendMessage(ChatColor.RED + "You don't have the permission to use this command");
				return true;
			}

			if (sender instanceof Player){
				sender.sendMessage(ChatColor.RED + "Looks like no update is available, you may need to restart your server.");
			}else{
				sender.sendMessage("Only players can use the update command.");
			}
			return true;
		}

		// debug commands
		if (!sender.hasPermission("betteruhc.commands.debug")){
			sender.sendMessage(ChatColor.RED + "You don't have the permission to use BetterUHC debug commands");
			return true;
		}

		if (args.length == 0){
			sender.sendMessage("Invalid command");
			return true;
		}

		PlayerManager pm = gameManager.getPlayerManager();

		switch(args[0]){
			case "gamestate":
				if(args.length == 2){
					try{
						GameState gameState = GameState.valueOf(args[1].toUpperCase());
						gameManager.setGameState(gameState);
						sender.sendMessage("Changed gamestate to: " + gameState.toString());
						return true;
					}catch(IllegalArgumentException e){
						sender.sendMessage(args[1]+" is not a valid game state");
						return true;
					}
				}else {
					sender.sendMessage("Current gamestate: " + gameManager.getGameState());
					return true;
				}
			case "playerstate":
				if(args.length == 3){
					try{
						Player player = Bukkit.getPlayer(args[1]);
						if(player == null){
							sender.sendMessage("Player "+args[1]+" is not online");
							return true;
						}
						PlayerState playerState = PlayerState.valueOf(args[2].toUpperCase());
						pm.getUhcPlayer(player).setState(playerState);
						sender.sendMessage("Changed " + player.getName() + "'s playerstate to " + playerState);
						return true;
					}catch(IllegalArgumentException e){
						sender.sendMessage(args[2]+" is not a valid player state");
						return true;
					}catch(Exception e){
						sender.sendMessage(e.getMessage());
						return true;
					}
				}else {
					sender.sendMessage("Invalid playerstate command");
					return true;
				}

			case "pvp":
				if(args.length == 2){
					boolean state = Boolean.parseBoolean(args[1]);
					gameManager.setPvp(state);
					sender.sendMessage("Changed PvP to " + state);
				}else {
					sender.sendMessage("Invalid pvp command");
				}
				return true;

			case "listplayers":
				listUhcPlayers(sender);
				return true;

			case "listteams":
				listUhcTeams(sender);
				return true;

			case "pause":
				String pauseState = PreStartThread.togglePause();
				sender.sendMessage("The starting thread state is now : "+pauseState);
				return true;

			case "force":
				String forceState = PreStartThread.toggleForce();
				sender.sendMessage("The starting thread state is now : "+forceState);
				return true;

			case "location":
				if (sender instanceof Player){
					sender.sendMessage(((Player) sender).getLocation().toString());
				}else{
					sender.sendMessage("Only players can use this sub-command.");
				}
				return true;
		}

		sender.sendMessage("Unknown sub command " + args[0]);
		return true;
	}


	private void listUhcPlayers(CommandSender sender) {
		StringBuilder str = new StringBuilder();
		str.append("Current UhcPlayers : ");
		for(UhcPlayer player : gameManager.getPlayerManager().getPlayersList()){
			str.append(player.getName());
			str.append(" ");
		}
		sender.sendMessage(str.toString());
	}

	private void listUhcTeams(CommandSender sender) {
		StringBuilder str;
		Bukkit.getLogger().info("Current UhcTeams : ");

		for(UhcTeam team : gameManager.getPlayerManager().listUhcTeams()){
			str = new StringBuilder();
			str.append("Team ").append(team.getLeader().getName()).append(" : ");
			for(UhcPlayer player : team.getMembers()){
				str.append(player.getName()).append(" ");
			}
			sender.sendMessage(str.toString());
		}
	}

}