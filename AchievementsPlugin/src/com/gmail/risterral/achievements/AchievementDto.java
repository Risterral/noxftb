package com.gmail.risterral.achievements;

import com.gmail.risterral.achievements.requirements.AbstractRequirement;
import com.gmail.risterral.achievements.requirements.RequirementType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class AchievementDto implements Callable {
    private long id;
    private String name;
    private boolean hidden;
    private String type;
    private int points;
    private String description;
    private List<AbstractRequirement> requirements;
    private RewardDto reward;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<AbstractRequirement> getRequirements() {
        return requirements;
    }

    public void setRequirements(List<AbstractRequirement> requirements) {
        this.requirements = requirements;
    }

    public RewardDto getReward() {
        return reward;
    }

    public void setReward(RewardDto reward) {
        this.reward = reward;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AchievementDto that = (AchievementDto) o;

        if (hidden != that.hidden) return false;
        if (id != that.id) return false;
        if (points != that.points) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (requirements != null ? !requirements.equals(that.requirements) : that.requirements != null) return false;
        if (reward != null ? !reward.equals(that.reward) : that.reward != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (hidden ? 1 : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + points;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (requirements != null ? requirements.hashCode() : 0);
        result = 31 * result + (reward != null ? reward.hashCode() : 0);
        return result;
    }

    @Override
    public Object call() throws Exception {
        return null;
    }

    @Override
    public String toString() {
        return "AchievementDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", hidden=" + hidden +
                ", type='" + type + '\'' +
                ", points=" + points +
                ", description='" + description + '\'' +
                ", requirements=" + requirements +
                ", reward=" + reward +
                '}';
    }
}
