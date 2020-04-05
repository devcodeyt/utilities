package me.devcode.utilities.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class BlockPlayerCommandListener implements Listener {

    @EventHandler
    public void handlePlayerCommand(PlayerCommandPreprocessEvent event) {

        String[] cmd = event.getMessage().substring(1).split(" ");

        if (cmd[0].equalsIgnoreCase("bukkit") ||
                cmd[0].equalsIgnoreCase("me") ||
                cmd[0].equalsIgnoreCase("help") ||
                cmd[0].equalsIgnoreCase("?") ||
                cmd[0].equalsIgnoreCase("pl") ||
                cmd[0].equalsIgnoreCase("plugins")) {
            event.setCancelled(true);
        }
    }
}
