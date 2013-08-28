package com.gmail.risterral.achievements.requirements;

import java.util.HashMap;

public abstract class AbstractRequirement {
    private long achievementId;
    private RequirementType type;

    public abstract boolean updateStatus(HashMap<String, Object> status);

    public abstract boolean isRequirementCompleted();

    public abstract double getRequirementProgress();

    @Override
    public abstract String toString();


    private static AbstractRequirement parseRequirement(String requirement) {
        Integer index = requirement.indexOf("type=") + 6;
        String type = requirement.substring(index, requirement.indexOf(",", index));
        if ("Die".equals(type)) {
            return new DieRequirement(requirement);
        }
        return null;
    }

    public long getAchievementId() {
        return achievementId;
    }

    public void setAchievementId(long achievementId) {
        this.achievementId = achievementId;
    }

    public RequirementType getType() {
        return type;
    }

    public void setType(RequirementType type) {
        this.type = type;
    }
}
