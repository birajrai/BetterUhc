package me.birajrai.scoreboard.placeholders;

import me.birajrai.exceptions.UhcPlayerNotOnlineException;
import me.birajrai.players.UhcPlayer;
import me.birajrai.scoreboard.Placeholder;
import me.birajrai.scoreboard.ScoreboardType;
import org.bukkit.entity.Player;

public class BlocksToTeamLeader extends Placeholder {

    public BlocksToTeamLeader(){
        super("blocksToTeamLeader");
    }

    @Override
    public String getReplacement(UhcPlayer uhcPlayer, Player player, ScoreboardType scoreboardType, String placeholder){
        if (uhcPlayer.getTeam().getMembers().size() == 1){
            return "0";
        }

        UhcPlayer teamMember = uhcPlayer.getTeam().getLeader();

        try {
            int distance = ((Double) teamMember.getPlayer().getLocation().distance(player.getLocation())).intValue();
            return String.valueOf(distance);
        }catch (UhcPlayerNotOnlineException ex){
            return "?";
        }
    }

}