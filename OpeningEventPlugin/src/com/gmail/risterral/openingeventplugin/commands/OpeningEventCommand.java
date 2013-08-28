package com.gmail.risterral.openingeventplugin.commands;

import com.gmail.risterral.openingeventplugin.OpeningEventPlugin;
import com.gmail.risterral.openingeventplugin.openingevent.OpeningEvent;
import com.gmail.risterral.openingeventplugin.openingevent.OpeningEventRequirement;
import com.gmail.risterral.openingeventplugin.openingevent.OpeningEventsPresenter;
import com.gmail.risterral.openingeventplugin.utils.MultiLineInfo;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.*;

public class OpeningEventCommand implements CommandExecutor {
    private static final String COMMANDS_LIST = "/openingevent [page]\n/openingevent about\n/openingevent list [page]\n/openingevent details {id} [page]\n/openingevent progress {name}\n/register event {id} {event_name}\n/register redstone emitter {id}\n/register requirement {id} {item} {ultimate_amount}\n/unregister event {id}\n/unregister redstone emitter {id} {number}\n/unregister requirement {id} {number}\n/unregister sign {id} {number}";
    private static final String ABOUT_DESCRIPTION = "Plugin made as tool to create opening events. To see commands list, type in '/openingevent [page]'";
    private static final String ABOUT_KEY = "about";
    private static final String LIST_KEY = "list";
    private static final String DETAILS_KEY = "details";
    private static final String PROGRESS_KEY = "progress";
    private static final String TELEPORT_KEY = "teleport";

    private Plugin plugin;

    public OpeningEventCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] split) {
        if (!(sender instanceof Player)) {
            return false;
        }
        OpeningEventsPresenter openingEventsPresenter = ((OpeningEventPlugin) plugin).getOpeningEventsPresenter();
        Player player = (Player) sender;

        if (split.length == 0) {
            MultiLineInfo.sendInfo(player, "Opening event plugin", "openingevent", COMMANDS_LIST.split("\\n"), 1);
            return true;
        } else if (split.length == 1 && split[0].matches("^\\d+$")) {
            Integer page = Integer.parseInt(split[0]);
            MultiLineInfo.sendInfo(player, "Opening event plugin", "openingevent", COMMANDS_LIST.split("\\n"), page);
            return true;
        } else if (split.length == 1 && ABOUT_KEY.equals(split[0])) {
            player.sendRawMessage(ABOUT_DESCRIPTION);
            return true;
        } else if ((split.length == 1 || split.length == 2) && LIST_KEY.equals(split[0])) {
            StringBuilder eventsListSB = new StringBuilder();
            for (Location key : openingEventsPresenter.getOpeningEvents().keySet()) {
                eventsListSB.append(ChatColor.YELLOW);
                eventsListSB.append(openingEventsPresenter.getOpeningEvents().get(key).getId());
                eventsListSB.append(ChatColor.GRAY);
                eventsListSB.append(": ");
                eventsListSB.append(ChatColor.GREEN);
                eventsListSB.append(openingEventsPresenter.getOpeningEvents().get(key).getName());
                eventsListSB.append("\n");
            }
            Integer page = 1;
            if (split.length == 2) {
                try {
                    page = Integer.parseInt(split[1]);
                } catch (NumberFormatException e) {
                    return false;
                }
            }
            MultiLineInfo.sendInfo(player, "Opening events list", null, eventsListSB.toString().split("\\n"), page);
            return true;
        } else if ((split.length == 2 || split.length == 3) && DETAILS_KEY.equals(split[0])) {
            try {
                Integer id = Integer.parseInt(split[1]);
                OpeningEvent openingEvent = openingEventsPresenter.getOpeningEventById(id);
                if (openingEvent == null) {
                    player.sendRawMessage(ChatColor.RED + "There is no registered event with " + id + " id.");
                    return true;
                }
                Integer page = 1;
                if (split.length == 3) {
                    if (split[2].matches("^\\d+$")) {
                        page = Integer.parseInt(split[2]);
                    } else {
                        player.sendRawMessage(ChatColor.RED + split[2] + " is not acceptable page number.");
                        return true;
                    }
                }

                StringBuilder openingEventSB = new StringBuilder();
                openingEventSB.append(createPropertyString("Id", openingEvent.getId().toString()));
                openingEventSB.append(createPropertyString("Name", openingEvent.getName()));
                openingEventSB.append(createPropertyString("Chest location",  locationToString(openingEvent.getChestLocation())));
                openingEventSB.append(createPropertyString("Complete message",  openingEvent.getCompleteMessage()));
                Integer iterator = 0;
                for (Location location : openingEvent.getRedstoneEmittersLocations()) {
                    iterator++;
                    openingEventSB.append(createPropertyString("Redstone emitter " + iterator, locationToString(location)));
                }
                iterator = 0;
                for (OpeningEventRequirement requirement : openingEvent.getRequirements()) {
                    iterator++;
                    openingEventSB.append(createPropertyString("Requirement " + iterator, requirementToString(requirement)));
                }
                iterator = 0;
                for (Location location : openingEvent.getSignsLocations()) {
                    iterator++;
                    openingEventSB.append(createPropertyString("Sign " + iterator, locationToString(location)));
                }

                MultiLineInfo.sendInfo(player, openingEvent.getName() + " event", null, openingEventSB.toString().split("\\n"), page);
                return true;
            } catch (NumberFormatException e) {
                player.sendRawMessage(ChatColor.RED + split[1] + " is not acceptable event id.");
                return true;
            }
        } else if (split.length > 1 && PROGRESS_KEY.equalsIgnoreCase(split[0])) {
            StringBuilder nameSB = new StringBuilder();
            for (int i = 1; i < split.length; i++) {
                nameSB.append(split[i]);
                nameSB.append(" ");
            }
            nameSB.deleteCharAt(nameSB.length() - 1);
            String name = nameSB.toString();

            OpeningEvent openingEvent = openingEventsPresenter.getOpeningEventByName(name);
            if (openingEvent == null) {
                player.sendRawMessage(ChatColor.RED + "There is no registered event named: \"" + name + "\".");
                return true;
            }

            player.sendRawMessage(ChatColor.YELLOW + name + " is completed in " + ChatColor.GREEN + openingEvent.getCompletedPercent() + "%");
            return true;
        } else if (split.length == 2 && TELEPORT_KEY.equalsIgnoreCase(split[0])) {
            try {
                Integer id = Integer.parseInt(split[1]);
                OpeningEvent openingEvent = openingEventsPresenter.getOpeningEventById(id);
                if (openingEvent == null) {
                    player.sendRawMessage(ChatColor.RED + "There is no registered event with " + id + " id.");
                    return true;
                }
                Location chestLocation = openingEvent.getChestLocation();
                Location location;
                Block block = chestLocation.getBlock();
                player.sendRawMessage(block.getFace(block).getModX() + " " + block.getFace(block).getModY() + " " + block.getFace(block).getModZ());
                return true;
            } catch (NumberFormatException e) {
                player.sendRawMessage(ChatColor.RED + split[1] + " is not acceptable event id.");
                return true;
            }
        }
        return false;
    }

    private String createPropertyString(String property, String value) {
        StringBuilder result = new StringBuilder();
        result.append(ChatColor.WHITE);
        result.append(property);
        result.append(ChatColor.GRAY);
        result.append(": ");
        result.append(ChatColor.YELLOW);
        result.append(value);
        result.append("\n");

        return result.toString();
    }

    private String locationToString(Location location) {
        StringBuilder result = new StringBuilder();
        result.append(location.getWorld().getName());
        result.append("  ");
        result.append(location.getBlockX());
        result.append(" ");
        result.append(location.getBlockY());
        result.append(" ");
        result.append(location.getBlockZ());

        return result.toString();
    }

    private String requirementToString(OpeningEventRequirement requirement) {
        StringBuilder result = new StringBuilder();
        result.append(requirement.getRequiredItem());
        result.append("  ");
        result.append(requirement.getPresentAmount());
        result.append("/");
        result.append(requirement.getUltimateAmount());

        return result.toString();
    }
}
