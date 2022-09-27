package me.birajrai.configuration;

import me.birajrai.utils.ProtocolUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class Dependencies {

    // dependencies
    private static boolean worldEditLoaded;
    private static boolean vaultLoaded;
    private static boolean protocolLibLoaded;

    public static void loadWorldEdit() {
        Plugin wePlugin = Bukkit.getPluginManager().getPlugin("WorldEdit");
        if(wePlugin == null || !wePlugin.getClass().getName().equals("com.sk89q.worldedit.bukkit.WorldEditPlugin")) {
            Bukkit.getLogger().warning("[BetterUHC] WorldEdit plugin not found, there will be no support of schematics.");
            worldEditLoaded = false;
        }else {
            Bukkit.getLogger().info("[BetterUHC] Hooked with WorldEdit plugin.");
            worldEditLoaded = true;
        }
    }

    public static void loadVault(){
        Plugin vault = Bukkit.getPluginManager().getPlugin("Vault");
        if(vault == null || !vault.getClass().getName().equals("net.milkbowl.vault.Vault")) {
            Bukkit.getLogger().warning("[BetterUHC] Vault plugin not found, there will be no support of economy rewards.");
            vaultLoaded = false;
            return;
        }

        Bukkit.getLogger().info("[BetterUHC] Hooked with Vault plugin.");
        vaultLoaded = true;

        VaultManager.setupEconomy();
    }

    public static void loadProtocolLib(){
        Plugin protocolLib = Bukkit.getPluginManager().getPlugin("ProtocolLib");
        if(protocolLib == null || !protocolLib.getClass().getName().equals("com.comphenix.protocol.ProtocolLib")) {
            Bukkit.getLogger().warning("[BetterUHC] ProtocolLib plugin not found.");
            protocolLibLoaded = false;
            return;
        }

        Bukkit.getLogger().info("[BetterUHC] Hooked with ProtocolLib plugin.");
        protocolLibLoaded = true;

        try {
            ProtocolUtils.register();
        }catch (Exception ex){
            protocolLibLoaded = false;
            Bukkit.getLogger().severe("[BetterUHC] Failed to load ProtocolLib, are you using the right version?");
            ex.printStackTrace();
        }
    }

    public static boolean getWorldEditLoaded() {
        return worldEditLoaded;
    }

    public static boolean getVaultLoaded() {
        return vaultLoaded;
    }

    public static boolean getProtocolLibLoaded(){
        return protocolLibLoaded;
    }

}
