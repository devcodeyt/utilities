package me.devcode.utilities.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Iterator;

import me.devcode.utilities.player.CustomPlayer;
import me.devcode.utilities.player.PlayerAPI;
import me.devcode.utilities.rank.Rank;

public class TablistManager {

    private static TablistManager instance;
    private Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

    public static TablistManager getInstance() {
        if (instance == null) {
            instance = new TablistManager();
        }

        return instance;
    }

    public void registerTeams() {
        Rank[] ranks = Rank.values();
        int size = ranks.length;

        for (int i = 0; i < size; i++) {
            Rank all = ranks[i];
            this.scoreboard.registerNewTeam(all.getTeamName());
            this.scoreboard.getTeam(all.getTeamName()).setPrefix(all.getTabPrefix());
        }

    }

    public void setTablistPrefix(Player player) {
        CustomPlayer aioPlayer = PlayerAPI.getInstance().getPlayer(player.getUniqueId());
        if (aioPlayer.isNick()) {
            this.resetPlayer(player);
            this.scoreboard.getTeam(Rank.PREMIUM.getTeamName()).addPlayer(player);
            player.setDisplayName(this.scoreboard.getTeam(Rank.PREMIUM.getTeamName()).getPrefix() + player.getName());
        } else {
            this.resetPlayer(player);
            this.scoreboard.getTeam(aioPlayer.getRank().getTeamName()).addPlayer(player);
            player.setDisplayName(this.scoreboard.getTeam(aioPlayer.getRank().getTeamName()).getPrefix() + player.getName());
        }

        Iterator iterator = Bukkit.getOnlinePlayers().iterator();

        while (iterator.hasNext()) {
            Player all = (Player) iterator.next();
            all.setScoreboard(this.scoreboard);
        }

    }

    public void registerTeam(String team, String prefix) {
        if (this.scoreboard.getTeam(team) == null) {
            this.scoreboard.registerNewTeam(team);
            this.scoreboard.getTeam(team).setPrefix(prefix);
        }

    }

    public void setTeamPrefix(Player player, String team) {
        this.resetPlayer(player);
        this.scoreboard.getTeam(team).addPlayer(player);
        player.setDisplayName(this.scoreboard.getTeam(team).getPrefix() + player.getName());
        Iterator iterator = Bukkit.getOnlinePlayers().iterator();

        while (iterator.hasNext()) {
            Player all = (Player) iterator.next();
            all.setScoreboard(this.scoreboard);
        }

    }

    public String getTeam(Player player) {
        return this.scoreboard.getPlayerTeam(player) != null ? this.scoreboard.getPlayerTeam(player).getName() : null;
    }

    public void resetPlayer(Player player) {
        if (this.getTeam(player) != null) {
            this.scoreboard.getTeam(this.getTeam(player)).removePlayer(player);
            player.setDisplayName(player.getName());
        }

    }

    public Scoreboard getScoreboard() {
        return this.scoreboard;
    }


}
