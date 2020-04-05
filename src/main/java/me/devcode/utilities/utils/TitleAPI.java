package me.devcode.utilities.utils;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PlayerConnection;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

import me.devcode.utilities.Utilities;

public class TitleAPI {

    private static TitleAPI instance;

    public static TitleAPI getInstance() {
        if (instance == null) {
            instance = new TitleAPI();
        }

        return instance;
    }

    public void sendTitle(Player player, String title, String subtitle, int fadein, int stay, int fadeout) {
        PlayerConnection connection = ((CraftPlayer)player).getHandle().playerConnection;
        IChatBaseComponent Ititle = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + title + "\"}");
        IChatBaseComponent Isub = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + subtitle + "\"}");
        PacketPlayOutTitle titletime = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TIMES, Ititle, fadein, stay, fadeout);
        PacketPlayOutTitle subtitletime = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TIMES, Isub);
        PacketPlayOutTitle titlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, Ititle);
        PacketPlayOutTitle subtitlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, Isub);
        connection.sendPacket(titletime);
        connection.sendPacket(subtitletime);
        connection.sendPacket(titlePacket);
        connection.sendPacket(subtitlePacket);
    }

    public void endTitel(Player p, String message) {


        PlayerConnection con = ((CraftPlayer)p).getHandle().playerConnection;
        IChatBaseComponent chat = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}");
        PacketPlayOutChat packet = new PacketPlayOutChat(chat, (byte) 2);
        con.sendPacket(packet);
    }


}
