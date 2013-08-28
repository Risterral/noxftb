package com.noxftb.www.votinghelperplugin.database;

import org.bukkit.plugin.java.JavaPlugin;

public class DatabasePresenter {

    private static final String SCHEMA_NAME = "voting_helper_plugin";
    private static final String VOTING_TABLE_NAME = "voting";

    private final JavaPlugin plugin;
    private final MySQL database;

    public DatabasePresenter(JavaPlugin plugin) {
        this.plugin = plugin;
        database = new MySQL(plugin, "89.69.176.101", "3306", "test", "Risterral", "ZAQ!2wsx");
        database.openConnection();
        prepareDatabase();
    }

    private void prepareDatabase() {
        database.executeUpdate("CREATE DATABASE IF NOT EXISTS " + SCHEMA_NAME);
        database.executeUpdate("CREATE TABLE IF NOT EXISTS " + SCHEMA_NAME + "." + VOTING_TABLE_NAME +
                " ( id INT NOT NULL PRIMARY KEY," +
                " name VARCHAR(40) NOT NULL," +
                " voting_number id," +
                " last_voting_date DATETIME )");
    }

    public MySQL getDatabase() {
        return database;
    }
}
