package com.gmail.risterral.openingeventplugin.listeners;

import com.gmail.risterral.openingeventplugin.OpeningEventPlugin;
import com.gmail.risterral.openingeventplugin.database.DatabasePresenter;
import com.gmail.risterral.openingeventplugin.openingevent.OpeningEvent;
import com.gmail.risterral.openingeventplugin.openingevent.OpeningEventRequirement;
import com.gmail.risterral.openingeventplugin.openingevent.OpeningEventsPresenter;
import org.bukkit.*;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class InventoryListener implements Listener {
    private final Plugin plugin;
    private final OpeningEventsPresenter openingEventsPresenter;
    private final DatabasePresenter databasePresenter;

    public InventoryListener(Plugin plugin) {
        this.plugin = plugin;
        this.openingEventsPresenter = ((OpeningEventPlugin) plugin).getOpeningEventsPresenter();
        this.databasePresenter = ((OpeningEventPlugin) plugin).getDatabasePresenter();
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (event.getInventory().getHolder() instanceof Chest) {
            Chest chest = (Chest) event.getInventory().getHolder();
            OpeningEvent openingEvent = openingEventsPresenter.getOpeningEvents().get(chest.getLocation());
            if (openingEvent == null) {
                return;
            }
            if (openingEvent.getChestOpen()) {
                ((Player) event.getPlayer()).sendRawMessage(ChatColor.RED + "Someone is already using this chest.");
                event.setCancelled(true);
            } else {
                openingEvent.setChestOpen(true);
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory().getHolder() instanceof Chest) {
            Chest chest = (Chest) event.getInventory().getHolder();
            OpeningEvent openingEvent = openingEventsPresenter.getOpeningEvents().get(chest.getLocation());
            if (openingEvent == null) {
                return;
            }
            consumeItems(openingEvent, event.getInventory());
            openingEvent.setChestOpen(false);
        }
    }

    private void consumeItems(OpeningEvent openingEvent, Inventory inventory) {
        Boolean removed = false;

        for (OpeningEventRequirement requirement : openingEvent.getRequirements()) {
            if (requirement.getCompleted()) {
                continue;
            }
            List<ItemStack> itemsToDestroy = new ArrayList<ItemStack>();
            for (ItemStack itemStack : inventory.getContents()) {
                if (itemStack == null) {
                    continue;
                }
                if (requirement.getRequiredItem().contains(":")) {
                    if (!requirement.getRequiredItem().matches(itemStack.getTypeId() + ":" + itemStack.getDurability())) {
                        continue;
                    }
                } else {
                    if (!requirement.getRequiredItem().matches(itemStack.getTypeId() + "")) {
                        continue;
                    }
                }

                Integer remainingAmount = requirement.getUltimateAmount() - requirement.getPresentAmount();
                if (itemStack.getAmount() < remainingAmount) {
                    requirement.setPresentAmount(requirement.getPresentAmount() + itemStack.getAmount());
                    itemsToDestroy.add(itemStack);
                } else  if (itemStack.getAmount() == remainingAmount) {
                    requirement.setPresentAmount(requirement.getUltimateAmount());
                    itemsToDestroy.add(itemStack);
                    requirement.setCompleted(true);
                } else {
                    requirement.setPresentAmount(requirement.getUltimateAmount());
                    itemStack.setAmount(itemStack.getAmount() - remainingAmount);
                    requirement.setCompleted(true);
                }
                removed = true;
            }
            for (ItemStack itemStack : itemsToDestroy) {
                inventory.remove(itemStack);
            }
        }
        if (removed) {
            databasePresenter.updateOpeningEventRequirements(openingEvent.getId(), openingEvent.getRequirements());
            openingEvent.getChestLocation().getWorld().playEffect(openingEvent.getChestLocation(), Effect.MOBSPAWNER_FLAMES, 0);
            openingEvent.getChestLocation().getWorld().playEffect(openingEvent.getChestLocation(), Effect.SMOKE, 0);
            for (Location location : openingEvent.getSignsLocations()) {
                openingEvent.updateSign(location);
            }

            if (openingEvent.areAllRequirementCompleted()) {
                Bukkit.broadcastMessage(ChatColor.YELLOW + openingEvent.getCompleteMessage());
                for (Location location : openingEvent.getRedstoneEmittersLocations()) {
                    location.getBlock().setType(Material.REDSTONE_BLOCK);
                }
            }
        }
    }
}
