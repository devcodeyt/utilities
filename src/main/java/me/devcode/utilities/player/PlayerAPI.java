package me.devcode.utilities.player;

import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Getter;
import me.devcode.utilities.Utilities;
import me.devcode.utilities.mysql.MySQLUtils;
import me.devcode.utilities.mysql.PlayerManager;
import me.devcode.utilities.rank.Rank;

@Getter
public class PlayerAPI {

    private static PlayerAPI instance;
    private static MySQLUtils mySQLUtils;

    private WeakHashMap<UUID, PlayerManager> playerManagerHashMap = new WeakHashMap<>();

    private HashMap<String, LinkedHashMap<String, Object>> values = new HashMap<>();



    public static PlayerAPI getInstance() {
        if (instance == null) {
            instance = new PlayerAPI();
            mySQLUtils = Utilities.getInstance().getMySQLUtils();
        }

        return instance;
    }

    public PlayerManager getPlayerManager(UUID player) {
        return playerManagerHashMap.getOrDefault(player, null);
    }

    public void setValues(String s, LinkedHashMap<String, Object> v) {
        values.put(s, v);
    }

    public PlayerManager setPlayerManager(UUID player, String database, List<String> list, boolean rank, String order) {
        PlayerManager playerManager = new PlayerManager(database, player);
        playerManager.loadStats(list, rank, order);
        playerManagerHashMap.put(player, playerManager);
        return playerManager;
    }

    public PlayerManager setPlayerManager(UUID player, String database) {
        PlayerManager playerManager = new PlayerManager(database, player);
        playerManagerHashMap.put(player, playerManager);
        return playerManager;
    }

    private ConcurrentHashMap<UUID, CustomPlayer> customPlayer = new ConcurrentHashMap<>();


    public CustomPlayer loadPlayer(UUID uuid) {
        if(!Utilities.getInstance().getMySQLUtils().isUserExisting(uuid.toString(), "customplayer")) {
            return setPlayer(uuid);
        } else {
            String name = (String) Utilities.getInstance().getMySQLUtils().getValue(uuid.toString(), "customplayer", "name");
            String rank = (String) Utilities.getInstance().getMySQLUtils().getValue(uuid.toString(), "customplayer", "rank");
            int coins = (int) Utilities.getInstance().getMySQLUtils().getValue(uuid.toString(), "customplayer", "coins");
            long timeStamp = Long.valueOf((String) Utilities.getInstance().getMySQLUtils().getValue(uuid.toString(), "customplayer", "timestamp"));
            boolean nick = (boolean) Utilities.getInstance().getMySQLUtils().getValue(uuid.toString(), "customplayer", "nick");
            if(name == null) {
                return setPlayer(uuid);
            }
            if(!name.equalsIgnoreCase(Bukkit.getOfflinePlayer(uuid).getName())) {
                name = Bukkit.getOfflinePlayer(uuid).getName();
            }
            CustomPlayer customPlayer = new CustomPlayer(uuid, name, Rank.getRankByName(rank), coins, timeStamp, nick);
            this.customPlayer.put(uuid, customPlayer);
            return customPlayer;
        }
    }

    public CustomPlayer setPlayer(UUID uuid) {
        LinkedHashMap<String, Object> values = new LinkedHashMap<>();
        String name = Bukkit.getOfflinePlayer(uuid).getName();
        String rank = "Spieler";
        int coins = 100;
        long timeStamp = System.currentTimeMillis();
        boolean nick = false;
        values.put("uuid", uuid.toString());
        values.put("name", name);
        values.put("rank", rank);
        values.put("coins", coins);
        values.put("timestamp", timeStamp);
        values.put("nick", nick);
        Utilities.getInstance().getMySQLUtils().createUser(uuid.toString(), "customplayer", values);
        CustomPlayer customPlayer = new CustomPlayer(uuid, name, Rank.getRankByName(rank), coins, timeStamp, nick);
        this.customPlayer.put(uuid, customPlayer);
        return customPlayer;
    }

    public void updatePlayer(UUID uuid) {
        if(getPlayer(uuid) == null)
            return;
        CustomPlayer customPlayer = getPlayer(uuid);
        LinkedHashMap<String, Object> values = new LinkedHashMap<>();
        values.put("name", customPlayer.getName());
        values.put("rank", customPlayer.getRank().getName());
        values.put("coins", customPlayer.getCoins());
        values.put("timestamp", customPlayer.getTimeStamp());
        values.put("nick", customPlayer.isNick());
        Utilities.getInstance().getMySQLUtils().updatePlayer(uuid.toString(), "customplayer", values);
    }

    public void removePlayer(UUID uuid) {
        this.customPlayer.remove(uuid);
    }

    public CustomPlayer getPlayer(UUID uuid) {
        if(!customPlayer.containsKey(uuid))
            return loadPlayer(uuid);
        return customPlayer.get(uuid);
    }

}
