package com.gmail.risterral.achievements.requirements;

import com.gmail.risterral.achievements.AchievementsPresenter;
import com.gmail.risterral.utils.Position;

import java.util.HashMap;

public class DieRequirement extends AbstractRequirement {
    private String cause;
    private int amount;
    private int playerAmount;
    private Position where;
    private String killer;

    public DieRequirement() {
        playerAmount = 0;
    }

    public DieRequirement(String requirement) {
        Integer index = requirement.indexOf("achievementId=") + 14;
        this.setAchievementId(Long.parseLong(requirement.substring(index, requirement.indexOf(",", index))));

        this.setType(RequirementType.Die);

        index = requirement.indexOf("cause='", index) + 7;
        this.cause = requirement.substring(index, requirement.indexOf("',", index));

        index = requirement.indexOf("amount=", index) + 7;
        this.amount = Integer.parseInt(requirement.substring(index, requirement.indexOf(",", index)));

        index = requirement.indexOf("playerAmount=", index) + 13;
        this.playerAmount = Integer.parseInt(requirement.substring(index, requirement.indexOf(",", index)));

        index = requirement.indexOf("where=", index) + 6;
        String whereString = requirement.substring(index, requirement.indexOf(",", index));
        if ("null".equals(whereString)) {
            this.where = null;
        } else {
            this.where = new Position(requirement.substring(index, requirement.indexOf("}", index)));
        }

        index = requirement.indexOf("killer=", index) + 7;
        Integer tempEndIndex = requirement.indexOf("}", index);
        if (tempEndIndex == -1) {
            tempEndIndex = requirement.length();
        }
        String killerString = requirement.substring(index, tempEndIndex);
        if ("null".equals(killerString)) {
            this.killer = null;
        } else {
            this.killer = requirement.substring(index + 1, tempEndIndex - 1);
        }
    }

    @Override
    public boolean updateStatus(HashMap<String, Object> status) {
        if (status.get(AchievementsPresenter.CAUSE_KEY) instanceof String && cause.equalsIgnoreCase((String) status.get(AchievementsPresenter.CAUSE_KEY))
            && (where == null || (status.get(AchievementsPresenter.WHERE_KEY) instanceof String && where.equals(status.get(AchievementsPresenter.WHERE_KEY))))
            && (killer == null || (status.get(AchievementsPresenter.KILLER_KEY) instanceof String && killer.equalsIgnoreCase((String) status.get(AchievementsPresenter.KILLER_KEY))))) {
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
        return "DieRequirement{" +
                "achievementId=" + getAchievementId() +
                ", type=" + getType() +
                ", cause='" + cause + '\'' +
                ", amount=" + amount +
                ", playerAmount=" + playerAmount +
                ", where=" + where +
                ", killer='" + killer + '\'' +
                '}';
    }



    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getPlayerAmount() {
        return playerAmount;
    }

    public void setPlayerAmount(int playerAmount) {
        this.playerAmount = playerAmount;
    }

    public Position getWhere() {
        return where;
    }

    public void setWhere(Position where) {
        this.where = where;
    }

    public String getKiller() {
        return killer;
    }

    public void setKiller(String killer) {
        this.killer = killer;
    }
}
