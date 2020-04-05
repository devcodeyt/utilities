package me.devcode.utilities.utils;

import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Hologram {

    private String name;
    private Location location;
    private List<String> lines;
    private boolean spawned;
    private List<EntityArmorStand> armorStands = new ArrayList();

    public Hologram(String name, Location location, List<String> lines) {
        this.name = name;
        this.location = location;
        this.lines = lines;
        Iterator iterator = this.lines.iterator();

        while(iterator.hasNext()) {
            String all = (String)iterator.next();
            EntityArmorStand entity = new EntityArmorStand(((CraftWorld)this.location.getWorld()).getHandle(), this.location.getX(), this.location.getY(), this.location.getZ());
            entity.setInvisible(true);
            entity.setCustomNameVisible(true);
            entity.setGravity(false);
            entity.setCustomName(all);
            this.location.subtract(0.0D, 0.25D, 0.0D);
            this.armorStands.add(entity);
        }

    }

    public void spawn() {
        this.spawned = true;
        Iterator var1 = Bukkit.getOnlinePlayers().iterator();

        while(var1.hasNext()) {
            Player player = (Player)var1.next();
            Iterator iterator = this.armorStands.iterator();

            while(iterator.hasNext()) {
                EntityArmorStand armorStand = (EntityArmorStand)iterator.next();
                PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(armorStand);
                ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
            }
        }

    }

    public void spawn(Player player) {
        Iterator iterator = this.armorStands.iterator();

        while(iterator.hasNext()) {
            EntityArmorStand armorStand = (EntityArmorStand)iterator.next();
            PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(armorStand);
            ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
        }

    }

    public void despawn() {
        this.spawned = false;
        Iterator iterator = Bukkit.getOnlinePlayers().iterator();

        while(iterator.hasNext()) {
            Player player = (Player)iterator.next();
            Iterator iterator1 = this.armorStands.iterator();

            while(iterator1.hasNext()) {
                EntityArmorStand armorStand = (EntityArmorStand)iterator1.next();
                PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(new int[]{armorStand.getId()});
                ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
            }
        }

    }

    public void despawn(Player player) {
        Iterator iterator = this.armorStands.iterator();

        while(iterator.hasNext()) {
            EntityArmorStand armorStand = (EntityArmorStand)iterator.next();
            PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(new int[]{armorStand.getId()});
            ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
        }

    }

    public boolean isSpawned() {
        return this.spawned;
    }

    public String getName() {
        return this.name;
    }

    public List<EntityArmorStand> getArmorStands() {
        return this.armorStands;
    }

    public List<String> getLines() {
        return this.lines;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLines(List<String> lines) {
        this.lines = lines;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setName(String name) {
        this.name = name;
    }

}
