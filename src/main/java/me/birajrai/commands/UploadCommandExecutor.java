package me.birajrai.commands;

import me.birajrai.BetterUhc;
import me.birajrai.utils.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class UploadCommandExecutor implements CommandExecutor{

    private enum FileType{
        LOG("logs/latest.log", false),
        SERVER("server.properties", false),
        CONFIG("config.yml", true),
        STORAGE("storage.yml", true),
        KITS("kits.yml", true),
        CRAFTS("crafts.yml", true),
        LANG("lang.yml", true),
        SCOREBOARD("scoreboard.yml", true),
        FLOWERPOWER("flowerpower.yml", true),
        SCENARIOS("scenarios.yml", true);

        private final String path;
        private final boolean dataFolder;

        FileType(String path, boolean dataFolder){
            this.path = path;
            this.dataFolder = dataFolder;
        }

        public File getFile(){
            if (dataFolder){
                return new File(BetterUhc.getPlugin().getDataFolder(), path);
            }
            return new File(path);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if (args.length != 1){
            sender.sendMessage(ChatColor.RED + "Usage: /upload <file-type>");
            return true;
        }

        FileType fileType;

        try {
            fileType = FileType.valueOf(args[0].toUpperCase());
        }catch (IllegalArgumentException ex){
            sender.sendMessage(ChatColor.RED + "Invalid file type! Choose from this list:");
            for (FileType type : FileType.values()){
                sender.sendMessage(ChatColor.GREEN + " - " + type.name().toLowerCase());
            }
            return true;
        }

        sender.sendMessage(ChatColor.GREEN + "Uploading " + fileType.getFile() + " ...");

        String url;
        try {
            url = uploadFile(fileType);
        }catch (IOException ex){
            sender.sendMessage(ChatColor.RED + "Failed to upload file, check console for more details!");
            ex.printStackTrace();
            return true;
        }

        sender.sendMessage(ChatColor.DARK_GREEN + ChatColor.BOLD.toString() + "URL: " + ChatColor.GREEN + url);
        return true;
    }

    private String uploadFile(FileType fileType) throws IOException{
        File file = fileType.getFile();

        Bukkit.getLogger().info("[UhcCore] Uploading file: " + file);

        BufferedReader reader = new BufferedReader(new FileReader(file));

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null){
            if (fileType == FileType.LOG || fileType == FileType.SERVER){
                sb.append(line.replaceAll("([0-9]{1,3}\\.){3}[0-9]{1,3}", "**.**.**.**"));
            }else {
                sb.append(line);
            }

            sb.append('\n');
        }

        reader.close();

        String url = FileUtils.uploadTextFile(sb);
        Bukkit.getLogger().info("[UhcCore] Successfully uploaded file: " + file);
        return url;
    }

}