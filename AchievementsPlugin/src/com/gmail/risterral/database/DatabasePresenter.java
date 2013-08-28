package com.gmail.risterral.database;

import com.gmail.risterral.AchievementsPlugin;
import com.gmail.risterral.achievements.AchievementDto;
import com.gmail.risterral.achievements.AchievementsPresenter;
import com.gmail.risterral.achievements.RewardDto;
import com.gmail.risterral.achievements.RewardItem;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabasePresenter {

    private static final String SCHEMA_NAME = "achievements_plugin";
    private static final String PLAYERS_TABLE_NAME = "players";

    private final JavaPlugin plugin;
    private final MySQL database;
    private final AchievementsPresenter achievementsPresenter;

    public DatabasePresenter(JavaPlugin plugin) {
        this.plugin = plugin;
        this.achievementsPresenter = ((AchievementsPlugin) plugin).getAchievementsPresenter();
        database = new MySQL(plugin, "89.69.176.101", "3306", "test", "Risterral", "ZAQ!2wsx");
        database.openConnection();
        prepareDatabase();
    }

    private void prepareDatabase() {
        database.executeUpdate("CREATE DATABASE IF NOT EXISTS " + SCHEMA_NAME);
//        database.executeUpdate("USE " + SCHEMA_NAME);
        database.executeUpdate("CREATE TABLE IF NOT EXISTS " + SCHEMA_NAME + "." + PLAYERS_TABLE_NAME +
                " ( id INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                " name VARCHAR(20) NOT NULL," +
                " points INT NOT NULL," +
                " rewards TEXT)");
    }

    public List<AchievementDto> getPlayerAchievementsDone(String playerName) {
        try {
            if (database.executeQuery("SELECT id FROM " + SCHEMA_NAME + "." + PLAYERS_TABLE_NAME + " WHERE name = '" + playerName + "'").next()) {

                return new ArrayList<AchievementDto>();
            } else {
                database.executeUpdate("CREATE TABLE " + SCHEMA_NAME + "." + playerName + "_achievements" +
                        " ( id INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                        " achievement TEXT NOT NULL," +
                        " type INT NOT NULL)");
                database.executeUpdate("INSERT INTO " + SCHEMA_NAME + "." + PLAYERS_TABLE_NAME +
                        " (name, points)" +
                        " VALUES ('" + playerName + "', 0)");
                return new ArrayList<AchievementDto>();
            }
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }


    public List<AchievementDto> getPlayerAchievementsUndone(String playerName) {
        try {
            if (database.executeQuery("SELECT id FROM " + SCHEMA_NAME + "." + PLAYERS_TABLE_NAME + " WHERE name = '" + playerName + "'").next()) {
                List<AchievementDto> achievements = new ArrayList<AchievementDto>();
                ResultSet resultSet = database.executeQuery("SELECT achievement FROM " + SCHEMA_NAME + "." + playerName + "_achievements WHERE type = 0");
                while (resultSet.next()) {
                    achievements.add(achievementsPresenter.parseAchievementFromDatabase(resultSet.getString("achievement")));
                }
                return achievements;
            } else {
                database.executeUpdate("CREATE TABLE " + SCHEMA_NAME + "." + playerName + "_achievements" +
                        " ( id INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                        " achievement TEXT NOT NULL," +
                        " type INT NOT NULL)");
                database.executeUpdate("INSERT INTO " + SCHEMA_NAME + "." + PLAYERS_TABLE_NAME +
                        " (name, points)" +
                        " VALUES ('" + playerName + "', 0)");
                return new ArrayList<AchievementDto>();
            }
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

    public void insertAchievement(String playerName, String achievement) {
        database.executeUpdate("INSERT INTO " + SCHEMA_NAME + "." + playerName + "_achievements (achievement, type) VALUES ('" + achievement.replaceAll("'","''") + "', 0)");
    }

    public void updateAchievement(String playerName, Long achievementId, String achievement) {
        database.executeUpdate("UPDATE " + SCHEMA_NAME + "." + playerName + "_achievements SET achievement='" + achievement.replaceAll("'","''") + "' WHERE id=" + achievementId);
    }

    public void updateAchievementStatus(String playerName, Long achievementId, Integer status) {
        database.executeUpdate("UPDATE " + SCHEMA_NAME + "." + playerName + "_achievements SET type=" + status + " WHERE id=" + achievementId);
    }

    public void updatePlayerStatus(String playerName, Integer points, List<RewardItem> rewards) {
        database.executeUpdate("UPDATE " + SCHEMA_NAME + "." + PLAYERS_TABLE_NAME + " SET points=" + points + " WHERE name=" + playerName);
    }

    public MySQL getDatabase() {
        return database;
    }
}
