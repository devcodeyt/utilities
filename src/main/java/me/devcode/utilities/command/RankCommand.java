package me.devcode.utilities.command;


import de.dytanic.cloudnet.common.document.gson.JsonDocument;
import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.ext.bridge.BridgePlayerManager;
import de.dytanic.cloudnet.ext.bridge.player.ICloudPlayer;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devcode.utilities.player.PlayerAPI;
import me.devcode.utilities.rank.Rank;

public class RankCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length != 2)
            return true;
        if (commandSender.hasPermission("rank.give")) {

            String player = args[0];
            String rank = args[1];

            for (ICloudPlayer cloudPlayer : BridgePlayerManager.getInstance().getOnlinePlayers()) {
                if (cloudPlayer.getName().equalsIgnoreCase(player)) {
                    player = cloudPlayer.getUniqueId().toString();
                }
            }

            JsonDocument document = new JsonDocument();
            document.append("player", player);
            document.append("rank", rank);


            CloudNetDriver.getInstance().sendChannelMessage("update-rank", "update", document);
            commandSender.sendMessage("Du hast dem Spieler " + player + " den Rang gegeben.");
            return true;
        }
        if(commandSender instanceof Player) {
            Player player1 = (Player) commandSender;
            if(PlayerAPI.getInstance().getPlayer(player1.getUniqueId()).hasMinRank(Rank.DEVELOPER)) {
                String player = args[0];
                String rank = args[1];

                for (ICloudPlayer cloudPlayer : BridgePlayerManager.getInstance().getOnlinePlayers()) {
                    if (cloudPlayer.getName().equalsIgnoreCase(player)) {
                        player = cloudPlayer.getUniqueId().toString();
                    }
                }

                JsonDocument document = new JsonDocument();
                document.append("player", player);
                document.append("rank", rank);


                CloudNetDriver.getInstance().sendChannelMessage("update-rank", "update", document);
                commandSender.sendMessage("Du hast dem Spieler " + player + " den Rang gegeben.");
                return true;
            }
        }

        return true;
    }
}
