package com.gmail.risterral.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

/**
 * Handler for the /pos sample command.
 * @author SpaceManiac
 */
public class SamplePosCommand implements CommandExecutor {

    private Plugin plugin;

    public SamplePosCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] split) {
        if (!(sender instanceof Player)) {
            return false;
        }
        Player player = (Player) sender;

        if (split.length == 0) {
            Location location = player.getLocation();
            player.sendMessage("You are currently at " + location.getX() +"," + location.getY() + "," + location.getZ() +
                    " with " + location.getYaw() + " yaw and " + location.getPitch() + " pitch :P");
            return true;
        } else if (split.length == 1) {
            plugin.getLogger().info("Metadata size: " + player.getMetadata("Achievements").size());
            for (MetadataValue achievements : player.getMetadata("Achievements")) {
                plugin.getLogger().info("Test: " + achievements.toString());
            }

            return true;
        } else if (split.length == 3) {
            try {
                double x = Double.parseDouble(split[0]);
                double y = Double.parseDouble(split[1]);
                double z = Double.parseDouble(split[2]);

                player.teleport(new Location(player.getWorld(), x, y, z));
            } catch (NumberFormatException ex) {
                player.sendMessage("Given location is invalid");
            }
            return true;
        } else {
            return false;
        }
    }
}
