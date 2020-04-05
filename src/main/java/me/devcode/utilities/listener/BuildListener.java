package me.devcode.utilities.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import me.devcode.utilities.Utilities;

public class BuildListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBreak(BlockBreakEvent e) {
        if(Utilities.getInstance().getBuild().contains(e.getPlayer()))
            e.setCancelled(false);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBreak(BlockPlaceEvent e) {
        if(Utilities.getInstance().getBuild().contains(e.getPlayer()))
            e.setCancelled(false);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBreak(InventoryClickEvent e) {
        if(Utilities.getInstance().getBuild().contains((Player) e.getWhoClicked()))
            e.setCancelled(false);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBreak(PlayerInteractEvent e) {
        if(Utilities.getInstance().getBuild().contains(e.getPlayer()))
            e.setCancelled(false);
    }


}
