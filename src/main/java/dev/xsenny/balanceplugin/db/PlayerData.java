package dev.xsenny.balanceplugin.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PlayerData {

    private String uuid;
    private long money;

    public PlayerData(String uuid, long money) {
        this.uuid = uuid;
        this.money = money;
    }

    public String getUuid() {
        return uuid;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
        try {
            PreparedStatement preparedStatement = Database.connection.prepareStatement("UPDATE players SET money = ? WHERE uuid = ?");
            preparedStatement.setLong(1, money);
            preparedStatement.setString(2, this.uuid);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
