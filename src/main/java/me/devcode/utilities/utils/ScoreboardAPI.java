package me.devcode.utilities.utils;

import net.minecraft.server.v1_8_R3.IScoreboardCriteria;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardDisplayObjective;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardObjective;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardScore;
import net.minecraft.server.v1_8_R3.Scoreboard;
import net.minecraft.server.v1_8_R3.ScoreboardObjective;
import net.minecraft.server.v1_8_R3.ScoreboardScore;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class ScoreboardAPI {

    private static ScoreboardAPI instance;

    public static ScoreboardAPI getInstance() {
        if (instance == null) {
            instance = new ScoreboardAPI();
        }

        return instance;
    }

    public void sendScoreboard(Player player, String name, String... lines) {
        this.sendScoreboard(player, name, Arrays.asList(lines));
    }

    public void sendScoreboard(Player player, String name, List<String> lines) {
        Scoreboard scoreboard = new Scoreboard();
        ScoreboardObjective objective = scoreboard.registerObjective(name, IScoreboardCriteria.b);
        PacketPlayOutScoreboardObjective removeScoreboard = new PacketPlayOutScoreboardObjective(objective, 1);
        PacketPlayOutScoreboardObjective createScoreboard = new PacketPlayOutScoreboardObjective(objective, 0);
        PacketPlayOutScoreboardDisplayObjective displayScoreboard = new PacketPlayOutScoreboardDisplayObjective(1, objective);
        objective.setDisplayName(name);
        this.sendPacket(player, removeScoreboard);
        this.sendPacket(player, createScoreboard);
        this.sendPacket(player, displayScoreboard);
        int count = lines.size();

        for(Iterator var10 = lines.iterator(); var10.hasNext(); --count) {
            String all = (String)var10.next();
            ScoreboardScore score = new ScoreboardScore(scoreboard, objective, all);
            score.setScore(count);
            PacketPlayOutScoreboardScore scoreboardScore = new PacketPlayOutScoreboardScore(score);
            this.sendPacket(player, scoreboardScore);
        }

    }

    public void sendScoreboard(Player player, String name, HashMap<Integer, String> lines) {
        Scoreboard scoreboard = new Scoreboard();
        ScoreboardObjective objective = scoreboard.registerObjective(name, IScoreboardCriteria.b);
        PacketPlayOutScoreboardObjective removeScoreboard = new PacketPlayOutScoreboardObjective(objective, 1);
        PacketPlayOutScoreboardObjective createScoreboard = new PacketPlayOutScoreboardObjective(objective, 0);
        PacketPlayOutScoreboardDisplayObjective displayScoreboard = new PacketPlayOutScoreboardDisplayObjective(1, objective);
        objective.setDisplayName(name);
        this.sendPacket(player, removeScoreboard);
        this.sendPacket(player, createScoreboard);
        this.sendPacket(player, displayScoreboard);
        Iterator var9 = lines.keySet().iterator();

        while(var9.hasNext()) {
            int i = (Integer)var9.next();
            ScoreboardScore score = new ScoreboardScore(scoreboard, objective, (String)lines.get(i));
            score.setScore(i);
            PacketPlayOutScoreboardScore scoreboardScore = new PacketPlayOutScoreboardScore(score);
            this.sendPacket(player, scoreboardScore);
        }

    }

    private void sendPacket(Player player, Packet packet) {
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
    }

}
