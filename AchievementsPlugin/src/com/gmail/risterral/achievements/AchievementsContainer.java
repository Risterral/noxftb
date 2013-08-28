package com.gmail.risterral.achievements;

import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class AchievementsContainer implements MetadataValue {

    private Plugin plugin;
    private List<AchievementDto> achievements = new ArrayList<AchievementDto>();


    public AchievementsContainer(Plugin plugin, List<AchievementDto> achievements) {
        this.plugin = plugin;
        this.achievements = achievements;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AchievementsContainer that = (AchievementsContainer) o;

        if (achievements != null ? !achievements.equals(that.achievements) : that.achievements != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return achievements != null ? achievements.hashCode() : 0;
    }

    @Override
    public Object value() {
        return achievements;
    }

    @Override
    public int asInt() {
        return 0;
    }

    @Override
    public float asFloat() {
        return 0;
    }

    @Override
    public double asDouble() {
        return 0;
    }

    @Override
    public long asLong() {
        return 0;
    }

    @Override
    public short asShort() {
        return 0;
    }

    @Override
    public byte asByte() {
        return 0;
    }

    @Override
    public boolean asBoolean() {
        return false;
    }

    @Override
    public String asString() {
        return null;
    }

    @Override
    public Plugin getOwningPlugin() {
        return plugin;
    }

    @Override
    public void invalidate() {
        return;
    }
}
