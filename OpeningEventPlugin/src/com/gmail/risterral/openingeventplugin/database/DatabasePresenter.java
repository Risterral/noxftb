package com.gmail.risterral.openingeventplugin.database;

import com.gmail.risterral.openingeventplugin.openingevent.OpeningEvent;
import com.gmail.risterral.openingeventplugin.openingevent.OpeningEventRequirement;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DatabasePresenter {

    private static final String SCHEMA_NAME = "opening_event_plugin";
    private static final String EVENTS_TABLE_NAME = "events";

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
        database.executeUpdate("CREATE TABLE IF NOT EXISTS " + SCHEMA_NAME + "." + EVENTS_TABLE_NAME +
                " ( id INT NOT NULL PRIMARY KEY," +
                " name VARCHAR(40) NOT NULL," +
                " location TEXT NOT NULL," +
                " complete_message TEXT," +
                " redstone_emitters TEXT," +
                " requirements TEXT," +
                " signs TEXT )");
    }

    public void insertOpeningEvent(Integer id, String name, Location location) {
        database.executeUpdate("INSERT INTO " + SCHEMA_NAME + "." + EVENTS_TABLE_NAME + " (id, name, location) VALUES (" + id + ", '" + name + "', '" + parseLocation(location) + "')");
    }

    public void updateCompleteMessage(Integer id, String completeMessage) {
        database.executeUpdate("UPDATE " + SCHEMA_NAME + "." + EVENTS_TABLE_NAME + " SET complete_message = '" + completeMessage + "' WHERE id = " + id);
    }

    public void deleteOpeningEvent(Integer id) {
        database.executeUpdate("DELETE FROM " + SCHEMA_NAME + "." + EVENTS_TABLE_NAME + " WHERE id = " + id);
    }

    public void updateOpeningEventRedstoneEmitters(Integer id, List<Location> redstoneEmitters) {
        database.executeUpdate("UPDATE " + SCHEMA_NAME + "." + EVENTS_TABLE_NAME + " SET redstone_emitters = '" + parseLocationsList(redstoneEmitters) + "' WHERE id = " + id);
    }

    public void updateOpeningEventRequirements(Integer id, List<OpeningEventRequirement> requirements) {
        database.executeUpdate("UPDATE " + SCHEMA_NAME + "." + EVENTS_TABLE_NAME + " SET requirements = '" + parseRequirements(requirements) + "' WHERE id = " + id);
    }

    public void updateOpeningEventSigns(Integer id, List<Location> signsLocations) {
        database.executeUpdate("UPDATE " + SCHEMA_NAME + "." + EVENTS_TABLE_NAME + " SET signs = '" + parseLocationsList(signsLocations) + "' WHERE id = " + id);
    }

    public HashMap<Location, OpeningEvent> getOpeningEvents() {
        HashMap<Location, OpeningEvent> result = new HashMap<Location, OpeningEvent>();
        if (!database.checkConnection()) {
            return result;
        }
        try {
            ResultSet resultSet = database.executeQuery("SELECT * FROM " + SCHEMA_NAME + "." + EVENTS_TABLE_NAME);
            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                Location location = parseLocation(resultSet.getString("location"));
                String completeMessage = resultSet.getString("complete_message");
                List<Location> redstoneEmitters = parseLocationsList(resultSet.getString("redstone_emitters"));
                List<OpeningEventRequirement> requirements = parseRequirements(resultSet.getString("requirements"));
                List<Location> signs = parseLocationsList(resultSet.getString("signs"));

                result.put(location, new OpeningEvent(id, name, location, completeMessage, redstoneEmitters, requirements, signs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    private String parseLocation(Location location) {
        StringBuilder result = new StringBuilder();
        result.append(location.getWorld().getName());
        result.append("&&");
        result.append(location.getBlockX());
        result.append("&&");
        result.append(location.getBlockY());
        result.append("&&");
        result.append(location.getBlockZ());

        return result.toString();
    }

    private Location parseLocation(String location) {
        String[] split = location.split("&&");
        World world = plugin.getServer().getWorld(split[0]);
        Double x = Double.parseDouble(split[1]);
        Double y = Double.parseDouble(split[2]);
        Double z = Double.parseDouble(split[3]);

        return new Location(world, x, y, z);
    }

    private String parseLocationsList(List<Location> locations) {
        StringBuilder result = new StringBuilder();
        for (Location location : locations) {
            result.append(parseLocation(location));
            result.append("**");
        }
        if (result.length() > 1) {
            result.delete(result.length() - 2, result.length());
        }

        return result.toString();
    }

    private List<Location> parseLocationsList(String locations) {
        List<Location> result = new ArrayList<Location>();
        if (locations == null) {
            return result;
        }
        for (String location : locations.split("\\*\\*")) {
            result.add(parseLocation(location));
        }

        return result;
    }

    private String parseRequirements(List<OpeningEventRequirement> requirements) {
        StringBuilder result = new StringBuilder();
        for (OpeningEventRequirement requirement : requirements) {
            result.append(requirement.getRequiredItem());
            result.append("&&");
            result.append(requirement.getPresentAmount());
            result.append("&&");
            result.append(requirement.getUltimateAmount());
            result.append("&&");
            result.append(requirement.getCompleted());
            result.append("**");
        }
        if (result.length() > 1) {
            result.delete(result.length() - 2, result.length());
        }
        return result.toString();
    }

    private List<OpeningEventRequirement> parseRequirements(String requirements) {
        List<OpeningEventRequirement> result = new ArrayList<OpeningEventRequirement>();
        if (requirements == null) {
            return result;
        }
        for (String requirement : requirements.split("\\*\\*")) {
            String[] split = requirement.split("&&");
            String requiredItem = split[0];
            Integer presentAmount = Integer.parseInt(split[1]);
            Integer ultimateAmount = Integer.parseInt(split[2]);
            Boolean isCompleted = "true".equals(split[3]);
            result.add(new OpeningEventRequirement(requiredItem, presentAmount, ultimateAmount, isCompleted));
        }

        return result;
    }

    public MySQL getDatabase() {
        return database;
    }
}
