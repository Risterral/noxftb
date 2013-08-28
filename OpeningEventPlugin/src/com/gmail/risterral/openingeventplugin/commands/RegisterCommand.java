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
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class RegisterCommand implements CommandExecutor {
    private static final String EVENT_KEY = "event";
    private static final String REDSTONE_KEY = "redstone";
    private static final String EMITTER_KEY = "emitter";
    private static final String REQUIREMENT_KEY = "requirement";
    private static final String COMPLETE_KEY = "complete";
    private static final String MESSAGE_KEY = "message";
    private static final String SIGN_KEY = "sign";

    private Plugin plugin;
    private final DatabasePresenter databasePresenter;

    public RegisterCommand(Plugin plugin) {
        this.plugin = plugin;
        this.databasePresenter = ((OpeningEventPlugin) plugin).getDatabasePresenter();
    }


    public boolean onCommand(CommandSender sender, Command command, String label, String[] split) {
        if (!(sender instanceof Player)) {
            return false;
        }
        Player player = (Player) sender;
        OpeningEventsPresenter openingEventsPresenter = ((OpeningEventPlugin) plugin).getOpeningEventsPresenter();

        if (split.length > 2 && EVENT_KEY.equalsIgnoreCase(split[0])) {
            try {
                Integer id = Integer.parseInt(split[1]);
                OpeningEvent openingEvent = openingEventsPresenter.getOpeningEventById(id);
                if (openingEvent != null) {
                    player.sendRawMessage(ChatColor.RED + "There is already registered event with " + id + " id.");
                    return true;
                }
                StringBuilder nameSB = new StringBuilder();
                for (int i = 2; i < split.length; i++) {
                    nameSB.append(split[i]);
                    nameSB.append(" ");
                }
                nameSB.deleteCharAt(nameSB.length() - 1);
                String name = nameSB.toString();
                Block block = player.getTargetBlock(null, 20);
                if (!block.getType().equals(Material.CHEST)) {
                    player.sendRawMessage(ChatColor.RED + "You have to be targeting chest, while registering event.");
                    return true;
                }
                Location location = block.getLocation();
                openingEvent = openingEventsPresenter.getOpeningEvents().get(location);
                if (openingEvent != null) {
                    player.sendRawMessage(ChatColor.RED + "Targeted chest is already registered.");
                    return true;
                }

                openingEvent = new OpeningEvent(id, name, location);
                databasePresenter.insertOpeningEvent(id, name, location);
                databasePresenter.updateCompleteMessage(id, openingEvent.getCompleteMessage());
                openingEventsPresenter.getOpeningEvents().put(location, openingEvent);
                player.sendRawMessage(ChatColor.GREEN + "The \"" + name + "\" event has been registered.");
                return true;
            } catch (NumberFormatException e) {
                player.sendRawMessage(ChatColor.RED + split[1] + " is not acceptable event id.");
                return true;
            }
        } else if (split.length > 3 && COMPLETE_KEY.equals(split[0]) && MESSAGE_KEY.equalsIgnoreCase(split[1])) {
            try {
                Integer id = Integer.parseInt(split[2]);
                OpeningEvent openingEvent = openingEventsPresenter.getOpeningEventById(id);
                if (openingEvent == null) {
                    player.sendRawMessage(ChatColor.RED + "There is no registered event with " + id + " id.");
                    return true;
                }
                StringBuilder messageSB = new StringBuilder();
                for (int i = 3; i < split.length; i++) {
                    messageSB.append(split[i]);
                    messageSB.append(" ");
                }
                messageSB.deleteCharAt(messageSB.length() - 1);
                databasePresenter.updateCompleteMessage(id, messageSB.toString());
                openingEvent.setCompleteMessage(messageSB.toString());
            } catch (NumberFormatException e) {
                player.sendRawMessage(ChatColor.RED + split[2] + " is not acceptable event id.");
                return true;
            }
        } else if (split.length == 3 && REDSTONE_KEY.equalsIgnoreCase(split[0]) && EMITTER_KEY.equalsIgnoreCase(split[1])) {
            try {
                Integer id = Integer.parseInt(split[2]);
                OpeningEvent openingEvent = openingEventsPresenter.getOpeningEventById(id);
                if (openingEvent == null) {
                    player.sendRawMessage(ChatColor.RED + "There is no registered event with " + id + " id.");
                    return true;
                }
                Block block = player.getTargetBlock(null, 20);
                if (openingEvent.getRedstoneEmittersLocations().contains(block.getLocation())) {
                    player.sendRawMessage(ChatColor.RED + "Targeted block is already registered as redstone emitter.");
                    return true;
                }

                openingEvent.getRedstoneEmittersLocations().add(block.getLocation());
                databasePresenter.updateOpeningEventRedstoneEmitters(id, openingEvent.getRedstoneEmittersLocations());
                player.sendRawMessage(ChatColor.GREEN + "Redstone emitter has been registered.");
                return true;
            } catch (NumberFormatException e) {
                player.sendRawMessage(ChatColor.RED + split[2] + " is not acceptable event id.");
                return true;
            }
        } else if (split.length == 4 && REQUIREMENT_KEY.equalsIgnoreCase(split[0])) {
            try {
                Integer id = Integer.parseInt(split[1]);
                OpeningEvent openingEvent = openingEventsPresenter.getOpeningEventById(id);
                if (openingEvent == null) {
                    player.sendRawMessage(ChatColor.RED + "There is no registered event with " + id + " id.");
                    return true;
                }
                String item = split[2];
                if (!item.matches("^\\d+(:\\d+)?$")) {
                    player.sendRawMessage(ChatColor.RED + split[2] + " is not acceptable item id.");
                    return true;
                }
                if (!split[3].matches("^\\d+$")) {
                    player.sendRawMessage(ChatColor.RED + split[3] + " is not acceptable item ultimate amount.");
                    return true;
                }
                Integer ultimateAmount = Integer.parseInt(split[3]);

                openingEvent.getRequirements().add(new OpeningEventRequirement(item, ultimateAmount));
                databasePresenter.updateOpeningEventRequirements(id, openingEvent.getRequirements());
                player.sendRawMessage(ChatColor.GREEN + "Requirement has been registered.");
                return true;
            } catch (NumberFormatException e) {
                player.sendRawMessage(ChatColor.RED + split[1] + " is not acceptable event id.");
                return true;
            }
        } else if (split.length == 2 && SIGN_KEY.equalsIgnoreCase(split[0])) {
            try {
                Integer id = Integer.parseInt(split[1]);
                OpeningEvent openingEvent = openingEventsPresenter.getOpeningEventById(id);
                if (openingEvent == null) {
                    player.sendRawMessage(ChatColor.RED + "There is no registered event with " + id + " id.");
                    return true;
                }
                Block block = player.getTargetBlock(null, 20);
                if (!block.getType().equals(Material.SIGN) && !block.getType().equals(Material.SIGN_POST) && !block.getType().equals(Material.WALL_SIGN)) {
                    player.sendRawMessage(ChatColor.RED + "You have to be targeting sign, while registering sign.");
                    return true;
                }

                openingEvent.getSignsLocations().add(block.getLocation());
                openingEvent.updateSign(block.getLocation());
                databasePresenter.updateOpeningEventSigns(id, openingEvent.getSignsLocations());
                player.sendRawMessage(ChatColor.GREEN + "Sign has been registered.");
                return true;
            } catch (NumberFormatException e) {
                player.sendRawMessage(ChatColor.RED + split[1] + " is not acceptable event id.");
                return true;
            }
        }
        return false;
    }
}
