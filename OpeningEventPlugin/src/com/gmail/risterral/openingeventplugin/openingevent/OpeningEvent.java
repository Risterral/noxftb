package com.gmail.risterral.openingeventplugin.openingevent;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;

import java.util.ArrayList;
import java.util.List;

public class OpeningEvent {
    private Integer id;
    private String name;
    private Location chestLocation;
    private String completeMessage;
    private List<Location> redstoneEmittersLocations = new ArrayList<Location>();
    private List<OpeningEventRequirement> requirements = new ArrayList<OpeningEventRequirement>();
    private List<Location> signsLocations = new ArrayList<Location>();
    private Boolean isChestOpen;

    public OpeningEvent(Integer id, String name, Location chestLocation) {
        this.id = id;
        this.name = name;
        this.chestLocation = chestLocation;
        this.completeMessage = "The \"" + name + "\" event has been completed.";
        this.isChestOpen = false;
    }

    public OpeningEvent(Integer id, String name, Location chestLocation, String completeMessage, List<Location> redstoneEmittersLocations, List<OpeningEventRequirement> requirements,  List<Location> signsLocations) {
        this.id = id;
        this.name = name;
        this.chestLocation = chestLocation;
        this.completeMessage = completeMessage;
        this.redstoneEmittersLocations = redstoneEmittersLocations;
        this.requirements = requirements;
        this.signsLocations = signsLocations;
        this.isChestOpen = false;
    }

    public Boolean areAllRequirementCompleted() {
        Boolean result = true;
        for (OpeningEventRequirement requirement : requirements) {
            result &= requirement.getCompleted();
        }
        return result;
    }

    public Double getCompletedPercent() {
        if (requirements.isEmpty()) {
            return 0.0;
        }
        Double presentAmount = 0.;
        Double totalAmount = 0.;
        for (OpeningEventRequirement requirement : requirements) {
            presentAmount += requirement.getPresentAmount();
            totalAmount += requirement.getUltimateAmount();
        }
        return ((double) Math.round( 1000 * presentAmount / totalAmount) / 10);
    }

    public void updateSign(Location location) {
        BlockState state = location.getBlock().getState();
        if (state instanceof Sign) {
            Double percent = getCompletedPercent();
            String progress;
            if (percent < 25) {
                progress = ChatColor.DARK_RED + percent.toString() + "%";
            } else if (percent < 50) {
                progress = ChatColor.YELLOW + percent.toString() + "%";
            } else if (percent < 75) {
                progress = ChatColor.DARK_GREEN + percent.toString() + "%";
            } else {
                progress = ChatColor.GREEN + percent.toString() + "%";
            }
            location.getWorld().playEffect(location, Effect.SMOKE, 0);
            Sign sign = (Sign) state;
            sign.setLine(0, "");
            sign.setLine(1, "Progress:");
            sign.setLine(2, progress);
            sign.setLine(3, "");
            sign.update();
        }
    }

    public void clearSigns() {
        for (Location signsLocation : signsLocations) {
            BlockState state = signsLocation.getBlock().getState();
            if (state instanceof Sign) {
                Sign sign = (Sign) state;
                sign.setLine(0, "");
                sign.setLine(1, "");
                sign.setLine(2, "");
                sign.setLine(3, "");
                sign.update();
            }
        }

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getChestLocation() {
        return chestLocation;
    }

    public void setChestLocation(Location chestLocation) {
        this.chestLocation = chestLocation;
    }

    public String getCompleteMessage() {
        return completeMessage;
    }

    public void setCompleteMessage(String completeMessage) {
        this.completeMessage = completeMessage;
    }

    public List<Location> getRedstoneEmittersLocations() {
        return redstoneEmittersLocations;
    }

    public void setRedstoneEmittersLocations(List<Location> redstoneEmittersLocations) {
        this.redstoneEmittersLocations = redstoneEmittersLocations;
    }

    public List<OpeningEventRequirement> getRequirements() {
        return requirements;
    }

    public void setRequirements(List<OpeningEventRequirement> requirements) {
        this.requirements = requirements;
    }

    public List<Location> getSignsLocations() {
        return signsLocations;
    }

    public void setSignsLocations(List<Location> signsLocations) {
        this.signsLocations = signsLocations;
    }

    public Boolean getChestOpen() {
        return isChestOpen;
    }

    public void setChestOpen(Boolean chestOpen) {
        isChestOpen = chestOpen;
    }
}
