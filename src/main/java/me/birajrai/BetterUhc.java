package me.birajrai;

import me.birajrai.game.GameManager;
import me.birajrai.utils.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class BetterUhc extends JavaPlugin{

	private static final int MIN_VERSION = 8;
	private static final int MAX_VERSION = 19;

	private static BetterUhc pl;
	private static int version;
	private GameManager gameManager;
	private Updater updater;

	@Override
	public void onEnable(){
		pl = this;

		loadServerVersion();

		gameManager = new GameManager();
		Bukkit.getScheduler().runTaskLater(this, () -> gameManager.loadNewGame(), 1);

		updater = new Updater(this);

		// Delete files that are scheduled for deletion
		FileUtils.removeScheduledDeletionFiles();
	}

	// Load the Minecraft version.
	private void loadServerVersion(){
		String versionString = Bukkit.getBukkitVersion();
		version = 0;

		for (int i = MIN_VERSION; i <= MAX_VERSION; i ++){
			if (versionString.contains("1." + i)){
				version = i;
			}
		}

		if (version == 0) {
			version = MIN_VERSION;
			Bukkit.getLogger().warning("[BetterUHC] Failed to detect server version! " + versionString + "?");
		}else {
			Bukkit.getLogger().info("[BetterUHC] 1." + version + " Server detected!");
		}
	}

	public static int getVersion() {
		return version;
	}
	
	public static BetterUhc getPlugin(){
		return pl;
	}

	@Override
	public void onDisable(){
		gameManager.getScenarioManager().disableAllScenarios();
		
		updater.runAutoUpdate();
		Bukkit.getLogger().info("[BetterUHC] Plugin disabled");
	}

}
