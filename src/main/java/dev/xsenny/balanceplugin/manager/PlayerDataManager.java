package dev.xsenny.balanceplugin.manager;

import dev.xsenny.balanceplugin.BalancePlugin;
import dev.xsenny.balanceplugin.db.Database;
import dev.xsenny.balanceplugin.db.PlayerData;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerDataManager {

    private List<PlayerData> playerDataList;

    public PlayerDataManager(BalancePlugin plugin) {
        playerDataList = new ArrayList<>();
        try {
            ResultSet rs = Database.onQuery(Database.connection.prepareStatement("SELECT * FROM players"));
            while (rs != null && rs.next()) {
                playerDataList.add(new PlayerData(rs.getString("uuid"), rs.getLong("money")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public PlayerData getPlayerData(UUID uuid) {
        return playerDataList.stream().filter(playerData -> playerData.getUuid().equals(uuid.toString())).toList().get(0);
    }

    public boolean doesAPlayerExist(UUID uuid) {
        return !playerDataList.stream().filter(playerData -> playerData.getUuid().equals(uuid.toString())).toList().isEmpty();
    }

    public PlayerData newPlayerData(UUID uuid) {
        PlayerData pd = new PlayerData(uuid.toString(), 0);
        try {
            PreparedStatement preparedStatement = Database.connection.prepareStatement("INSERT INTO players VALUES (?, ?)");
            preparedStatement.setString(1, uuid.toString());
            preparedStatement.setLong(2, 0);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        playerDataList.add(pd);
        return pd;
    }

}
