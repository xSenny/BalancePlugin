package dev.xsenny.balanceplugin.db;


import dev.xsenny.balanceplugin.BalancePlugin;

import java.io.File;
import java.sql.*;

public class Database {

    public static Connection connection;
    private static Statement statement;


    public static void connect(BalancePlugin plugin) {
        connection = null;
        try {
            File file = new File(plugin.getDataFolder(), "database.db");
            if (!file.exists())
                file.createNewFile();
            String url = "jdbc:sqlite:" + file.getPath();
            connection = DriverManager.getConnection(url);
            System.out.println("Connected to database");
            statement = connection.createStatement();
        } catch (SQLException | java.io.IOException e) {
            e.printStackTrace();
        }
    }

    public static void disconnect() {
        try {
            if (connection != null)
                connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void onUpdate(PreparedStatement preparedStatement) {
        try {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("EROARE DE EXECUTARE");
            e.printStackTrace();
        }
    }

    public static void onUpdate(String sql) {
        try {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("EROARE DE EXECUTARE");
            e.printStackTrace();
        }
    }

    public static ResultSet onQuery(PreparedStatement preparedStatement) {
        try {
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
