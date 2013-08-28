package com.gmail.risterral.openingeventplugin.database;

import org.bukkit.plugin.Plugin;

import java.sql.*;
import java.util.logging.Level;

public class MySQL {
    private final Plugin plugin;
    private final String user;
    private final String database;
    private final String password;
    private final String port;
    private final String hostname;

    private Connection connection;

    public MySQL(Plugin plugin, String hostname, String port, String database, String username, String password) {
        this.plugin = plugin;
        this.hostname = hostname;
        this.port = port;
        this.database = database;
        this.user = username;
        this.password = password;
        this.connection = null;
    }

    public Connection openConnection() {
        try {
//            Class.forName("com.mysql.JDBC.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + this.hostname + ":" + this.port + "/" + this.database, this.user, this.password);
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not connect to MySQL server! because: " + e.getMessage());
        }// catch (ClassNotFoundException e) {
//            plugin.getLogger().log(Level.SEVERE, "JDBC Driver not found!");
//        }
        return connection;
    }

    public boolean checkConnection() {
        return connection != null;
    }

    public Connection getConnection() {
        return connection;
    }

    public ResultSet executeQuery(String sql) {
        if (this.checkConnection()) {
            try {
                Statement statement = connection.createStatement();
                return statement.executeQuery(sql);
            } catch (SQLException e) {
                plugin.getLogger().log(Level.SEVERE, "Could not execute query: " + sql + " because " + e.getMessage());
            }
        }
        return null;
    }

    public Integer executeUpdate(String sql) {
        if (this.checkConnection()) {
            try {
                Statement statement = connection.createStatement();
                return statement.executeUpdate(sql);
            } catch (SQLException e) {
                plugin.getLogger().log(Level.SEVERE, "Could not execute query: " + sql + " because " + e.getMessage());
            }
        }
        return null;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                plugin.getLogger().log(Level.SEVERE, "Error closing the MySQL Connection!");
                e.printStackTrace();
            }
        }
    }
}