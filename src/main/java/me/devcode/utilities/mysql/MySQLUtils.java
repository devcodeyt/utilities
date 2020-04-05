package me.devcode.utilities.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

import lombok.SneakyThrows;
import me.devcode.utilities.Utilities;

public class MySQLUtils {


    public boolean isUserExisting(String uuid, String table) {
        return Utilities.getInstance().getMySQLUtils().getBooleanMethod(table, "uuid", uuid);
    }

    //Create new user with a hashmap for people who are not familiar with preparedstatements
    //Else just use the mysqlapi
    @SneakyThrows
    public void createUser(String uuid, String table, LinkedHashMap<String, Object> values) {
        if (!isUserExisting(uuid, table)) {
            Set<String> values2 = values.keySet();
            int i = 0;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("INSERT INTO " + table + "(");
            for (String s : values2) {
                if (i < values2.size() - 1) {
                    stringBuilder.append(s + ", ");
                } else {
                    stringBuilder.append(s + ") VALUES (");
                }
                i++;
            }
            i = 0;
            while (i < values2.size()) {
                if (i < values2.size() - 1) {
                    stringBuilder.append("?,");
                } else {
                    stringBuilder.append("?);");
                }
                i++;
            }
            PreparedStatement preparedStatement = Utilities.getInstance().getMySQL().prepare(stringBuilder.toString());
            i = 1;
            for (String s : values2) {
                preparedStatement.setObject(i, values.get(s));
                i++;
            }
            Utilities.getInstance().getMySQL().update(preparedStatement);
        }
    }

    @SneakyThrows
    public void createUser(String uuid, String table, PreparedStatement preparedStatement) {
        if (!isUserExisting(uuid, table)) {
            Utilities.getInstance().getMySQL().update(preparedStatement);
        }
    }


    public Object getValue(String uuid, String table, String key) {
        return getObject(table, "uuid", uuid, key);
    }

    public boolean getBooleanMethod(String table, String from, String uuid) {
        boolean contains = false;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            ps = Utilities.getInstance().getMySQL().prepare("SELECT " + from + " FROM " + table + " WHERE " + from + "=?");
            ps.setString(1, uuid);

            rs = ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            while (rs.next()) {
                if (rs != null) {
                    contains = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contains;
    }

    @SneakyThrows
    public Object getObject(String table, String from, String uuid, String get) {
        PreparedStatement statement;
        ResultSet rs;
        statement = Utilities.getInstance().getMySQL()
                .prepare("SELECT " + get +" FROM " + table +" WHERE " + from +"=?;");
        statement.setString(1, uuid);
        rs = statement.executeQuery();

        Object object = null;
        while (rs.next()) {
            if (rs != null) {
                object = rs.getObject(get);
                break;
            }
        }
        statement.close();
        return object;
    }

    @SneakyThrows
    public void setObject(String table, String from, String uuid, Object object, String set) {
        PreparedStatement preparedStatement = Utilities.getInstance().getMySQL().prepare("UPDATE " + table + " SET " + set + " = ? WHERE " + from + " = ?;");
        preparedStatement.setObject(1, object);
        preparedStatement.setString(2, uuid);
        Utilities.getInstance().getMySQL().update(preparedStatement);
    }


    //Updating Player if the person dont know how to use mysql (preparedstaments) else just use the api
    @SneakyThrows
    public void updatePlayer(String uuid, String table, LinkedHashMap<String, Object> values) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("UPDATE " + table + " SET ");
        Set<String> values2 = values.keySet();
        int i = 0;
        for (String s : values2) {
            if (i < values2.size() - 1) {
                stringBuilder.append(s + " = ?, ");
            } else {
                stringBuilder.append(s + " = ? WHERE uuid = ?;");
            }
            i++;
        }
        PreparedStatement preparedStatement =
                Utilities.getInstance().getMySQL().prepare(stringBuilder.toString());
        i = 1;
        for (String s : values2) {
            preparedStatement.setObject(i, values.get(s));
            i++;
        }
        preparedStatement.setString(i, uuid);
        Utilities.getInstance().getMySQL().update(preparedStatement);

    }


}
