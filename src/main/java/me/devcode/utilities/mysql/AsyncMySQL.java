package me.devcode.utilities.mysql;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.logging.Logger;

import lombok.Getter;
import lombok.SneakyThrows;

public class AsyncMySQL {
    @Getter
    public ExecutorService executor;
    private Plugin plugin;
    private MySQL sql;

    public AsyncMySQL(Plugin plugin, String host, int port, String user, String password, String database) {
        try {
            sql = new MySQL(host, port, user, password, database);
            executor = Executors.newCachedThreadPool();
            this.plugin = plugin;
            Logger.getLogger("").info("MySQL > Connected.");
        } catch (Exception e) {
            Logger.getLogger("").info("Error. Couldnt connect to your MySQL-DB.");
            Bukkit.shutdown();
            return;
        }
    }
    public void update(PreparedStatement statement) {
        executor.execute(() ->
            sql.queryUpdate(statement));
    }

    public void updateNoClosing(PreparedStatement statement) {
        executor.execute(() ->
                sql.queryUpdate(statement));
    }

    public void update(String statement) {
        executor.execute(() -> sql.queryUpdate(statement));

    }

    public void query(PreparedStatement statement, Consumer<ResultSet> consumer) {
        executor.execute(() -> {
            ResultSet result = sql.query(statement);
            Bukkit.getScheduler().runTask(plugin, () -> consumer.accept(result));
        });
    }

    public void query(String statement, Consumer<ResultSet> consumer) {
        executor.execute(() -> {
            ResultSet result = sql.query(statement);
            Bukkit.getScheduler().runTask(plugin, () -> consumer.accept(result));
        });
    }

    public PreparedStatement prepare(String query) {
        try {
            return sql.getConnection().prepareStatement(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public MySQL getMySQL() {
        return sql;
    }


    public static class MySQL {

        private String host, user, password, database;
        private int port;

        private Connection conn;

        public MySQL(String host, int port, String user, String password, String database) throws Exception {
            this.host = host;
            this.port = port;
            this.user = user;
            this.password = password;
            this.database = database;

            this.openConnection();
        }


        public void queryUpdate(String query) {
            PreparedStatement statement = null;
            try {
                statement = conn.prepareStatement(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            queryUpdate(statement);
        }



        public void queryUpdate(PreparedStatement statement) {
            try {
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        public void queryUpdateNoClosing(PreparedStatement statement) {
            try {
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @SneakyThrows
        public ResultSet query(String query) {
            return query(conn.prepareStatement(query));
        }

        @SneakyThrows
        public ResultSet query(PreparedStatement statement) {
            return statement.executeQuery();

        }

        public Connection getConnection() {
            return this.conn;
        }
        @SneakyThrows
        public void checkConnection() {
                if (this.conn == null || !this.conn.isValid(10) || this.conn.isClosed())
                    openConnection();
        }


        public Connection openConnection() throws Exception {
            Class.forName("com.mysql.jdbc.Driver");
            return this.conn = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database + "?autoReconnect=true", this.user, this.password);
        }

        public void closeConnection() {
            try {
                this.conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                this.conn = null;
            }
        }
    }


}

