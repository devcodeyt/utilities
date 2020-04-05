package me.devcode.utilities.mysql;

import org.bukkit.scheduler.BukkitRunnable;

import java.sql.PreparedStatement;
import java.util.UUID;

import lombok.SneakyThrows;
import me.devcode.utilities.Utilities;
import me.devcode.utilities.player.PlayerAPI;

public class MySQLBenchmark {

    /*
    Testing MySQL Async
     */

    @SneakyThrows
    public MySQLBenchmark() {
        TimeMeter timeMeter = new TimeMeter("mysqlnew");
        AsyncMySQL asyncMySQL = Utilities.getInstance().getMySQL();
        PreparedStatement preparedStatement = asyncMySQL.prepare("INSERT INTO customplayer(uuid, name, rank, coins, timestamp, nick) VALUES (?, ?, ?, ?, ?, ?);");
        String uuid = UUID.randomUUID().toString();
        preparedStatement.setString(1, uuid);
        preparedStatement.setString(2, "C");
        preparedStatement.setString(3, "Spieler");
        preparedStatement.setInt(4, 100);
        preparedStatement.setLong(5, 1L);
        preparedStatement.setBoolean(6, false);
        asyncMySQL.update(preparedStatement);


      //  PlayerAPI.getInstance().loadPlayer(UUID.randomUUID());
        for (int i = 0; i < 10; i++) {
            System.out.println(Utilities.getInstance().getMySQLUtils().getObject("customplayer", "uuid", uuid, "coins"));

            timeMeter.test("nr " + i);

        }
        timeMeter.test("end");
        test();

    }

    private void test() {
        new BukkitRunnable() {

            @Override
            public void run() {
                test();
            }
        }.runTaskLater(Utilities.getInstance(), 100);
    }

}
