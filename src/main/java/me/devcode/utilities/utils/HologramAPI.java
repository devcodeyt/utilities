package me.devcode.utilities.utils;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class HologramAPI {

    private static HologramAPI instance;
    private List<Hologram> holograms = new ArrayList();

    public static HologramAPI getInstance() {
        if (instance == null) {
            instance = new HologramAPI();
        }

        return instance;
    }

    public List<Hologram> getHolograms() {
        return this.holograms;
    }

    public Hologram getHologram(String name) {
        Iterator iterator = this.holograms.iterator();

        Hologram hologram;
        do {
            if (!iterator.hasNext()) {
                return null;
            }

            hologram = (Hologram) iterator.next();
        } while (!hologram.getName().equals(name));

        return hologram;
    }

    public Hologram createHologram(String name, Location location, List<String> lines) {
        if (this.getHologram(name) == null) {
            Hologram hologram = new Hologram(name, location, lines);
            this.holograms.add(hologram);
            return hologram;
        } else {
            return null;
        }
    }

    public void destroyHologram(String name) {
        if (this.getHologram(name) != null) {
            Hologram hologram = this.getHologram(name);
            this.holograms.remove(hologram);
        }

    }

}
