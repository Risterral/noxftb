package com.gmail.risterral.player;

import com.gmail.risterral.achievements.AchievementDto;
import com.gmail.risterral.achievements.RewardItem;
import com.gmail.risterral.achievements.requirements.RequirementType;
import com.gmail.risterral.achievements.RewardDto;

import java.util.List;

public class PlayerDto {
    private String name;
    private List<AchievementDto> achievementsDone;
    private List<AchievementDto> achievementsUndone;
    private int points;
    private List<RewardItem> obtainableRewards;
    private List<RequirementType> eventsToCheck;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AchievementDto> getAchievementsDone() {
        return achievementsDone;
    }

    public void setAchievementsDone(List<AchievementDto> achievementsDone) {
        this.achievementsDone = achievementsDone;
    }

    public List<AchievementDto> getAchievementsUndone() {
        return achievementsUndone;
    }

    public void setAchievementsUndone(List<AchievementDto> achievementsUndone) {
        this.achievementsUndone = achievementsUndone;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public List<RewardItem> getObtainableRewards() {
        return obtainableRewards;
    }

    public void setObtainableRewards(List<RewardItem> obtainableRewards) {
        this.obtainableRewards = obtainableRewards;
    }

    public List<RequirementType> getEventsToCheck() {
        return eventsToCheck;
    }

    public void setEventsToCheck(List<RequirementType> eventsToCheck) {
        this.eventsToCheck = eventsToCheck;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlayerDto playerDto = (PlayerDto) o;

        if (points != playerDto.points) return false;
        if (achievementsDone != null ? !achievementsDone.equals(playerDto.achievementsDone) : playerDto.achievementsDone != null)
            return false;
        if (achievementsUndone != null ? !achievementsUndone.equals(playerDto.achievementsUndone) : playerDto.achievementsUndone != null)
            return false;
        if (eventsToCheck != null ? !eventsToCheck.equals(playerDto.eventsToCheck) : playerDto.eventsToCheck != null)
            return false;
        if (name != null ? !name.equals(playerDto.name) : playerDto.name != null) return false;
        if (obtainableRewards != null ? !obtainableRewards.equals(playerDto.obtainableRewards) : playerDto.obtainableRewards != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (achievementsDone != null ? achievementsDone.hashCode() : 0);
        result = 31 * result + (achievementsUndone != null ? achievementsUndone.hashCode() : 0);
        result = 31 * result + points;
        result = 31 * result + (obtainableRewards != null ? obtainableRewards.hashCode() : 0);
        result = 31 * result + (eventsToCheck != null ? eventsToCheck.hashCode() : 0);
        return result;
    }
}
