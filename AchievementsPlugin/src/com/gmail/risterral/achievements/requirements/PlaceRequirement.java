package com.gmail.risterral.achievements.requirements;

import com.gmail.risterral.achievements.AchievementsPresenter;
import com.gmail.risterral.utils.Position;

import java.util.HashMap;

public class PlaceRequirement extends AbstractRequirement {
    private String whatItem;
    private Integer amount;
    private Integer playerAmount;
    private Position where;


    public PlaceRequirement() {
        playerAmount = 0;
    }

    public PlaceRequirement(String requirement) {
        Integer index = requirement.indexOf("achievementId=") + 14;
        this.setAchievementId(Long.parseLong(requirement.substring(index, requirement.indexOf(",", index))));

        this.setType(RequirementType.Place);

        index = requirement.indexOf("whatItem='", index) + 10;
        this.whatItem = requirement.substring(index, requirement.indexOf("',", index));

        index = requirement.indexOf("amount=", index) + 7;
        this.amount = Integer.parseInt(requirement.substring(index, requirement.indexOf(",", index)));

        index = requirement.indexOf("playerAmount=", index) + 13;
        this.playerAmount = Integer.parseInt(requirement.substring(index, requirement.indexOf(",", index)));

        index = requirement.indexOf("where=", index) + 6;
        Integer tempEndIndex = requirement.indexOf("}", index);
        if (tempEndIndex == -1) {
            tempEndIndex = requirement.length();
        }
        String whereString = requirement.substring(index, tempEndIndex);
        if ("null".equals(whereString)) {
            this.where = null;
        } else {
            this.where = new Position(requirement.substring(index, tempEndIndex));
        }
    }

    @Override
    public boolean updateStatus(HashMap<String, Object> status) {
        if (status.get(AchievementsPresenter.WHAT_KEY) instanceof String && whatItem.equalsIgnoreCase((String) status.get(AchievementsPresenter.WHAT_KEY))
                && (where == null || (status.get(AchievementsPresenter.WHERE_KEY) instanceof Position && where.equals(status.get(AchievementsPresenter.WHERE_KEY))))) {
            playerAmount += 1;
            return true;
        }
        return false;
    }

    @Override
    public boolean isRequirementCompleted() {
        return playerAmount == amount;
    }

    @Override
    public double getRequirementProgress() {
        return (double) playerAmount / amount;
    }

    @Override
    public String toString() {
        return "PlaceRequirement{" +
                "achievementId=" + getAchievementId() +
                ", type=" + getType() +
                ", whatItem='" + whatItem + '\'' +
                ", amount=" + amount +
                ", playerAmount=" + playerAmount +
                ", where=" + where +
                '}';
    }

    public String getWhatItem() {
        return whatItem;
    }

    public void setWhatItem(String whatItem) {
        this.whatItem = whatItem;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Position getWhere() {
        return where;
    }

    public void setWhere(Position where) {
        this.where = where;
    }
}
