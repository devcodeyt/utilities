package me.devcode.utilities.listener;

import de.dytanic.cloudnet.common.document.gson.JsonDocument;
import de.dytanic.cloudnet.driver.event.EventListener;
import de.dytanic.cloudnet.driver.event.events.channel.ChannelMessageReceiveEvent;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.util.UUID;

import me.devcode.utilities.mysql.PlayerManager;
import me.devcode.utilities.player.CustomPlayer;
import me.devcode.utilities.player.PlayerAPI;
import me.devcode.utilities.rank.Rank;

public class UpdateListener implements Listener {

    @EventListener
    public void onBukkitSubChannelMessage(ChannelMessageReceiveEvent event) {
        JsonDocument jsonDocument = event.getData();
        if (event.getChannel().equalsIgnoreCase("update-rank")) {

            UUID player = UUID.fromString(jsonDocument.getString("player"));
            String rank = jsonDocument.getString("rank");

            if (Bukkit.getPlayer(player) != null) {
                CustomPlayer customPlayer = PlayerAPI.getInstance().getPlayer(player);
                Rank rank1 = Rank.getRankByName(rank);
                if(customPlayer.getRank().getPriority() < rank1.getPriority()) {
                    Bukkit.getPlayer(player).sendMessage("§3Du hast den Rang " + rank1.getColor() + rank1.getName() + " §3erhalten. Herzlichen Glückwunsch!");
                }
                PlayerAPI.getInstance().getPlayer(player).setRank(rank1);

            }
        }

    }

}
