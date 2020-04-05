package me.devcode.utilities.utils;

import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import me.devcode.utilities.Utilities;

public class BungeeCord {

    private static BungeeCord instance;

    public static BungeeCord getInstance() {
        if (instance == null) {
            instance = new BungeeCord();
        }

        return instance;
    }

    public void sendToServer(Player p, String serverName) {
        try {

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(stream);
            out.writeUTF("Connect");
            out.writeUTF(serverName);
            p.sendPluginMessage(Utilities.getInstance(), "BungeeCord", stream.toByteArray());

        } catch(Exception localexeption) {}
    }

}
