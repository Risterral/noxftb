package com.gmail.risterral.achievements.requirements;

import java.util.HashMap;

public class SpecialRequirement extends AbstractRequirement {
    private String description;

    public SpecialRequirement() {
    }

    public SpecialRequirement(String requirement) {
        Integer index = requirement.indexOf("achievementId=") + 14;
        this.setAchievementId(Long.parseLong(requirement.substring(index, requirement.indexOf(",", index))));

        this.setType(RequirementType.Special);

        index = requirement.indexOf("description='", index) + 13;
        this.description = requirement.substring(index, requirement.indexOf("'", index));
    }


    @Override
    public boolean updateStatus(HashMap<String, Object> status) {
        return false;
    }

    @Override
    public boolean isRequirementCompleted() {
        return false;
    }

    @Override
    public double getRequirementProgress() {
        return 0;
    }

    @Override
    public String toString() {
        return "SpecialRequirement{" +
                "achievementId=" + getAchievementId() +
                ", type=" + getType() +
                ", description='" + description + '\'' +
                '}';
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
