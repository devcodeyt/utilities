package me.devcode.utilities.command;

import de.dytanic.cloudnet.driver.event.events.service.CloudServiceConnectNetworkEvent;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devcode.utilities.Utilities;
import me.devcode.utilities.player.CustomPlayer;
import me.devcode.utilities.player.PlayerAPI;
import me.devcode.utilities.rank.Rank;

public class Build implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        //Utilities.getInstance().getMySQL().update("DROP TABLE IF EXISTS customplayer");
        if(commandSender instanceof Player) {
            Player player = (Player) commandSender;
            CustomPlayer customPlayer = PlayerAPI.getInstance().getPlayer(player.getUniqueId());
            if(!customPlayer.hasMinRank(Rank.SUPPORTER))
                return true;
            if(args.length == 0) {
                    if(Utilities.getInstance().getBuild().contains(player)) {
                        Utilities.getInstance().getBuild().remove(player);
                        player.sendMessage("Du kannst nicht mehr bauen.");
                    } else {
                        Utilities.getInstance().getBuild().add(player);
                        player.sendMessage("Du kannst nun bauen.");
                    }

            } else if(args.length == 1) {
                Player target = Bukkit.getPlayer(args[0]);
                if(target == null) {
                    player.sendMessage("Spieler ist nicht online.");
                    return true;
                }
                if(Utilities.getInstance().getBuild().contains(target)) {
                    CustomPlayer customPlayer1 = PlayerAPI.getInstance().getPlayer(target.getUniqueId());
                    if(customPlayer.getRank().getPriority() < customPlayer1.getRank().getPriority()) {
                        player.sendMessage("Du kannst dem Spieler nicht die Build rechte wegnehmen.");
                        return true;
                    }
                    Utilities.getInstance().getBuild().remove(target);
                    player.sendMessage("Du kannst nicht mehr bauen.");
                } else {
                    Utilities.getInstance().getBuild().add(target);
                    player.sendMessage("Du kannst nun bauen.");
                }
            }
        }

        return true;
    }
}
