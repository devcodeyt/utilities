package me.devcode.utilities;

import de.dytanic.cloudnet.driver.CloudNetDriver;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import me.devcode.utilities.command.Build;
import me.devcode.utilities.command.RankCommand;
import me.devcode.utilities.listener.BlockPlayerCommandListener;
import me.devcode.utilities.listener.BuildListener;
import me.devcode.utilities.listener.ConnectionListener;
import me.devcode.utilities.listener.UpdateListener;
import me.devcode.utilities.mysql.AsyncMySQL;
import me.devcode.utilities.mysql.MySQLUtils;
import me.devcode.utilities.mysql.PlayerManager;
import me.devcode.utilities.player.PlayerAPI;
import me.devcode.utilities.utils.TablistManager;

@Getter
@Setter

public class Utilities extends JavaPlugin {

    @Getter
    private static Utilities instance;

    private AsyncMySQL mySQL;
    private MySQLUtils mySQLUtils;
    private File mySQLFile;
    private YamlConfiguration mySQLCfg;

    private ArrayList<Player> build = new ArrayList<>();

    @Override
    public void onEnable() {
        instance = this;
        init();

        registerCommands();
        loadMySQL();
        UUID uuid = UUID.randomUUID();
        createNewTable("CREATE TABLE IF NOT EXISTS test(uuid TEXT, coins int);");
        PlayerAPI.getInstance().loadPlayer(uuid);
        LinkedHashMap<String, Object> values = PlayerAPI.getInstance().getValues().getOrDefault("test", new LinkedHashMap<>());
        values.put("uuid",uuid.toString());
        PlayerManager playerManager = new PlayerManager("test", uuid);
        playerManager.register(values);
        playerManager.setValue("coins", 0);
        PlayerAPI.getInstance().setPlayerManager(uuid, playerManager.getDatabase());
        System.out.println(playerManager.getValue("coins"));
        registerListeners();
        new BukkitRunnable() {

            @Override
            public void run() {
                mySQL.getMySQL().checkConnection();
            }
        }.runTaskTimerAsynchronously(this, 20*60*5, 20*60*5);

    }

    @Override
    public void onDisable()
    {
        PlayerAPI.getInstance().getCustomPlayer().keySet().forEach(customPlayer ->
            PlayerAPI.getInstance().updatePlayer(customPlayer));
        PlayerAPI.getInstance().getPlayerManagerHashMap().keySet().forEach(uuid ->
                PlayerAPI.getInstance().getPlayerManager(uuid).setDataBack());

        mySQL.getMySQL().closeConnection();
    }

    private void init() {
        TablistManager.getInstance().registerTeams();
    }

    private void registerCommands() {
        getCommand("build").setExecutor(new Build());
        getCommand("rank").setExecutor(new RankCommand());
    }

    private void registerListeners() {
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new ConnectionListener(), this);
        pluginManager.registerEvents(new BuildListener(), this);
        pluginManager.registerEvents(new BlockPlayerCommandListener(), this);
        pluginManager.registerEvents(new UpdateListener(), this);
        CloudNetDriver.getInstance().getEventManager().registerListener(new UpdateListener());
    }

    @SneakyThrows
    private void loadMySQL() {
        mySQLUtils = new MySQLUtils();
        mySQLFile = createFile("mysql.yml", true, this.getDataFolder());
        mySQLCfg = YamlConfiguration.loadConfiguration(mySQLFile);
        mySQL = new AsyncMySQL(this, mySQLCfg.getString("mySQL.host"), mySQLCfg.getInt("mySQL.port"), mySQLCfg.getString("mySQL.user"), mySQLCfg.getString("mySQL.password"), mySQLCfg.getString("mySQL.database"));
        createNewTable("CREATE TABLE IF NOT EXISTS customplayer(uuid TEXT, name varchar(20), rank varchar(20), coins int, timestamp long, nick boolean);");
    }

    private File createFile(String name, boolean load, File folder) {
        if(!folder.mkdir())
            folder.mkdir();
        if(load && !new File(folder, name).exists())
            loadFile(name, folder);
        File file = new File(folder, name);

        return file;
    }

    @SneakyThrows
    private void loadFile(String file, File folder) {
        File t = new File(folder, file);
        System.out.println("Writing new file: " + t.getAbsolutePath());

        t.createNewFile();
        FileWriter out = new FileWriter(t);
        InputStream is = getClass().getResourceAsStream("/" + file);

        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line;
        while ((line = br.readLine()) != null) {
            out.write(line + "\n");
        }
        System.out.println(line);
        out.flush();
        is.close();
        isr.close();
        br.close();
        out.close();

    }

    public void createNewTable(String create) {
        mySQL.update(create);
    }

}
