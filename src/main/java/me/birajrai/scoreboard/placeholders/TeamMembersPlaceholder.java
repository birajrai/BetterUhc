package me.birajrai.scoreboard.placeholders;

import me.birajrai.exceptions.UhcPlayerNotOnlineException;
import me.birajrai.players.UhcPlayer;
import me.birajrai.scoreboard.Placeholder;
import me.birajrai.scoreboard.ScoreboardType;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TeamMembersPlaceholder extends Placeholder {

    private final Map<UUID, Integer> lastShownMember;

    public TeamMembersPlaceholder(){
        super("members", "members-name", "members-health");
        lastShownMember = new HashMap<>();
    }

    @Override
    public String getReplacement(UhcPlayer uhcPlayer, Player player, ScoreboardType scoreboardType, String placeholder){

        List<UhcPlayer> teamMembers;

        if (scoreboardType.equals(ScoreboardType.WAITING)){
            teamMembers = uhcPlayer.getTeam().getMembers();
        }else{
            teamMembers = uhcPlayer.getTeam().getMembers(UhcPlayer::isPlaying);
        }

        if (teamMembers.isEmpty()){
            return "-";
        }

        boolean namePlaceholder = placeholder.equals("members") || placeholder.equals("members-name");

        // Name placeholder
        if (namePlaceholder){
            int showPlayer = lastShownMember.getOrDefault(player.getUniqueId(), -1) + 1;
            if (showPlayer >= teamMembers.size()) {
                showPlayer = 0;
            }
            lastShownMember.put(player.getUniqueId(), showPlayer);
            return teamMembers.get(showPlayer).getRealName();
        }
        // Health placeholder
        else{
            int showPlayer = lastShownMember.getOrDefault(player.getUniqueId(), 0);

            try {
                return String.valueOf((int) teamMembers.get(showPlayer).getPlayer().getHealth());
            }catch (UhcPlayerNotOnlineException ex){
                return "?";
            }
        }
    }

}