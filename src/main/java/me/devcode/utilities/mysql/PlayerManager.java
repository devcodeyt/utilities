package me.devcode.utilities.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.SneakyThrows;
import me.devcode.utilities.Utilities;

@Getter
public class PlayerManager {

    private UUID uuid;
    private boolean dataBack = false;
    private LinkedHashMap<String, Object> values = new LinkedHashMap<>();
    private String database;

    public PlayerManager(String database, UUID uuid) {
        this.uuid = uuid;
        this.database = database;
    }

    public void addValue(String key, Integer value) {
        this.values.put(key, (int) this.values.getOrDefault(key, 0) + value);
    }

    public void removeValue(String key) {
        this.values.remove(key);
    }

    public void setValue(String key, Object value) {
        this.values.put(key, value);
    }

    public Object getValue(String key) {
        return this.values.getOrDefault(key, 0);
    }

    public void loadStats(List<String> values, boolean rank, String order) {
        values.forEach(dValues ->
                setValue(dValues, Utilities.getInstance().getMySQLUtils().getObject(this.getDatabase(), "uuid", uuid.toString(), dValues)));
    }

    public void setRank(final String order) {
        //this.setValue("rank", getRank(this.uuid.toString(), this.database, order));
        this.setValue("rank", 0);
    }
    @SneakyThrows
    public int getRank(final String uuid, final String table, final String order) {
        int count = 0;
        final ResultSet resultSet = null;
            final PreparedStatement preparedStatement = Utilities.getInstance().getMySQL().prepare("SELECT * FROM " + table + " ORDER BY " + order + " DESC;");
            preparedStatement.executeQuery();
            while (resultSet.next()) {
                count++;
                final String namedUUID = resultSet.getString("uuid");
                final UUID uuid2 = UUID.fromString(namedUUID);
                if (uuid2.toString().equals(uuid)) {
                    preparedStatement.close();
                    return count;
                }
            }
        return -1;
    }

    public void register(LinkedHashMap<String, Object> v) {
        values = v;
        Utilities.getInstance().getMySQLUtils().createUser(uuid.toString(), database, v);
    }

    public boolean exist() {

        return Utilities.getInstance().getMySQLUtils().isUserExisting(uuid.toString(), database);
    }

    public void setDataBack() {
        if (isDataBack())
            return;
        dataBack = true;

        Utilities.getInstance().getMySQLUtils().updatePlayer(uuid.toString(), database, values);
    }

}
