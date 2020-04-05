package me.devcode.utilities.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.devcode.utilities.Utilities;
import me.devcode.utilities.player.PlayerAPI;
import me.devcode.utilities.utils.HologramAPI;

public class ConnectionListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onLogin(AsyncPlayerPreLoginEvent e) {
        PlayerAPI.getInstance().loadPlayer(e.getUniqueId());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        if(!HologramAPI.getInstance().getHolograms().isEmpty()) {
            HologramAPI.getInstance().getHolograms().forEach(hologram -> {
                hologram.despawn(player);
            });
        }

        if(PlayerAPI.getInstance().getPlayer(player.getUniqueId()) != null) {
            PlayerAPI.getInstance().updatePlayer(player.getUniqueId());
            PlayerAPI.getInstance().removePlayer(player.getUniqueId());
        }

    }


}
