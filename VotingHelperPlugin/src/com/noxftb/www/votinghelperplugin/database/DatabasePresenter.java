package com.noxftb.www.votinghelperplugin.database;

import com.noxftb.www.votinghelperplugin.player.PlayerDto;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

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
                " ( id INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                " name VARCHAR(40) NOT NULL," +
                " voting_number id," +
                " last_voting_date DATETIME )");
    }

    public PlayerDto getVotingDetails(String playerName) {
        PlayerDto result = new PlayerDto();
        if (!database.checkConnection()) {
            return result;
        }

        try {
            ResultSet resultSet = database.executeQuery("SELECT * FROM " + SCHEMA_NAME + "." + VOTING_TABLE_NAME + " WHERE name = '" + playerName + "'");
            if (resultSet.next()) {
                result.setNumberOfVotes(resultSet.getInt("voting_number"));
                result.setLastVotingDate(resultSet.getDate("last_voting_date"));
            } else {
                addPlayerToDatabase(playerName);
                result.setNumberOfVotes(0);
            }
            result.setName(playerName);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public void updatePlayerVote(String playerName, Integer numberOfVotes, Date lastVotingDate) {
        database.executeUpdate("UPDATE " + SCHEMA_NAME + "." + VOTING_TABLE_NAME + " SET voting_number = " + numberOfVotes +
                ", last_voting_date = " + lastVotingDate + " WHERE name = '" + playerName + "'");
    }

    private void addPlayerToDatabase(String playerName) {
        database.executeUpdate("INSERT INTO " + SCHEMA_NAME + "." + VOTING_TABLE_NAME + " (name, voting_number) VALUES ('" + playerName + "', 0)");
    }

    public MySQL getDatabase() {
        return database;
    }
}
