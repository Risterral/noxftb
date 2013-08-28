package com.gmail.risterral.openingeventplugin.commands;

import com.gmail.risterral.openingeventplugin.OpeningEventPlugin;
import com.gmail.risterral.openingeventplugin.database.DatabasePresenter;
import com.gmail.risterral.openingeventplugin.openingevent.OpeningEvent;
import com.gmail.risterral.openingeventplugin.openingevent.OpeningEventRequirement;
import com.gmail.risterral.openingeventplugin.openingevent.OpeningEventsPresenter;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class UnregisterCommand implements CommandExecutor {
    private static final String EVENT_KEY = "event";
    private static final String REDSTONE_KEY = "redstone";
    private static final String EMITTER_KEY = "emitter";
    private static final String REQUIREMENT_KEY = "requirement";
    private static final String SIGN_KEY = "sign";

    private Plugin plugin;
    private final DatabasePresenter databasePresenter;

    public UnregisterCommand(Plugin plugin) {
        this.plugin = plugin;
        this.databasePresenter = ((OpeningEventPlugin) plugin).getDatabasePresenter();
    }


    public boolean onCommand(CommandSender sender, Command command, String label, String[] split) {
        if (!(sender instanceof Player)) {
            return false;
        }
        Player player = (Player) sender;
        OpeningEventsPresenter openingEventsPresenter = ((OpeningEventPlugin) plugin).getOpeningEventsPresenter();

        if (split.length == 2 && EVENT_KEY.equalsIgnoreCase(split[0])) {
            try {
                Integer id = Integer.parseInt(split[1]);
                OpeningEvent openingEvent = openingEventsPresenter.getOpeningEventById(id);
                if (openingEvent == null) {
                    player.sendRawMessage(ChatColor.RED + "There is no registered event with " + id + " id.");
                    return true;
                }
                String name = openingEvent.getName();

                databasePresenter.deleteOpeningEvent(id);
                openingEventsPresenter.getOpeningEvents().remove(openingEvent.getChestLocation());
                player.sendRawMessage(ChatColor.GREEN + "The \"" + name + "\" event has been unregistered.");
                return true;
            } catch (NumberFormatException e) {
                player.sendRawMessage(ChatColor.RED + split[1] + " is not acceptable event id.");
                return true;
            }
        } else if (split.length == 4 && REDSTONE_KEY.equalsIgnoreCase(split[0]) && EMITTER_KEY.equalsIgnoreCase(split[1])) {
            try {
                Integer id = Integer.parseInt(split[2]);
                OpeningEvent openingEvent = openingEventsPresenter.getOpeningEventById(id);
                if (openingEvent == null) {
                    player.sendRawMessage(ChatColor.RED + "There is no registered event with " + id + " id.");
                    return true;
                }
                if (!split[3].matches("^\\d+$")) {
                    player.sendRawMessage(ChatColor.RED + split[3] + " is not acceptable redstone emitter number.");
                    return true;
                }
                Integer redstoneEmitterNumber = Integer.parseInt(split[3]);
                if (redstoneEmitterNumber < 1 || redstoneEmitterNumber > openingEvent.getRedstoneEmittersLocations().size()) {
                    player.sendRawMessage(ChatColor.RED + redstoneEmitterNumber.toString() + " is not acceptable redstone emitter number.");
                    return true;
                }

                openingEvent.getRedstoneEmittersLocations().remove(openingEvent.getRedstoneEmittersLocations().get(redstoneEmitterNumber - 1));
                databasePresenter.updateOpeningEventRedstoneEmitters(id, openingEvent.getRedstoneEmittersLocations());
                player.sendRawMessage(ChatColor.GREEN + "Redstone emitter has been unregistered.");
                return true;
            } catch (NumberFormatException e) {
                player.sendRawMessage(ChatColor.RED + split[2] + " is not acceptable event id.");
                return true;
            }
        } else if (split.length == 3 && REQUIREMENT_KEY.equalsIgnoreCase(split[0])) {
            try {
                Integer id = Integer.parseInt(split[1]);
                OpeningEvent openingEvent = openingEventsPresenter.getOpeningEventById(id);
                if (openingEvent == null) {
                    player.sendRawMessage(ChatColor.RED + "There is no registered event with " + id + " id.");
                    return true;
                }
                String item = split[2];
                if (!item.matches("^\\d+$")) {
                    player.sendRawMessage(ChatColor.RED + split[2] + " is not acceptable requirement number.");
                    return true;
                }
                Integer requirementNumber = Integer.parseInt(split[2]);
                if (requirementNumber < 1 || requirementNumber > openingEvent.getRequirements().size()) {
                    player.sendRawMessage(ChatColor.RED + requirementNumber.toString() + " is not acceptable requirement number.");
                    return true;
                }

                openingEvent.getRequirements().remove(openingEvent.getRequirements().get(requirementNumber - 1));
                databasePresenter.updateOpeningEventRequirements(id, openingEvent.getRequirements());
                player.sendRawMessage(ChatColor.GREEN + "Requirement has been unregistered.");
                return true;
            } catch (NumberFormatException e) {
                player.sendRawMessage(ChatColor.RED + split[1] + " is not acceptable event id.");
                return true;
            }
        } else if (split.length == 3 && SIGN_KEY.equalsIgnoreCase(split[0])) {
            try {
                Integer id = Integer.parseInt(split[1]);
                OpeningEvent openingEvent = openingEventsPresenter.getOpeningEventById(id);
                if (openingEvent == null) {
                    player.sendRawMessage(ChatColor.RED + "There is no registered event with " + id + " id.");
                    return true;
                }
                String item = split[2];
                if (!item.matches("^\\d+$")) {
                    player.sendRawMessage(ChatColor.RED + split[2] + " is not acceptable sign number.");
                    return true;
                }
                Integer signNumber = Integer.parseInt(split[2]);
                if (signNumber < 1 || signNumber > openingEvent.getRequirements().size()) {
                    player.sendRawMessage(ChatColor.RED + signNumber.toString() + " is not acceptable sign number.");
                    return true;
                }

                openingEvent.getSignsLocations().remove(openingEvent.getSignsLocations().get(signNumber - 1));
                databasePresenter.updateOpeningEventSigns(id, openingEvent.getSignsLocations());
                player.sendRawMessage(ChatColor.GREEN + "Sign has been unregistered.");
                return true;
            } catch (NumberFormatException e) {
                player.sendRawMessage(ChatColor.RED + split[1] + " is not acceptable event id.");
                return true;
            }
        }
        return false;
    }
}
