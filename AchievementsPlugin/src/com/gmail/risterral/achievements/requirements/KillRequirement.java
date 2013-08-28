package com.gmail.risterral.achievements.requirements;

import com.gmail.risterral.achievements.AchievementsPresenter;
import com.gmail.risterral.utils.ParsingUtil;
import com.gmail.risterral.utils.Position;

import java.util.HashMap;

public class KillRequirement extends AbstractRequirement {
    private String what;
    private Integer amount;
    private Integer playerAmount;
    private Position where;
    private String weapon;

    public KillRequirement() {
        playerAmount = 0;
    }

    public KillRequirement(String requirement) {
        Integer index = requirement.indexOf("achievementId=") + 14;
        this.setAchievementId(Long.parseLong(requirement.substring(index, requirement.indexOf(",", index))));

        this.setType(RequirementType.Kill);

        index = requirement.indexOf("what='", index) + 6;
        this.what = ParsingUtil.parseString(requirement.substring(index, requirement.indexOf("',", index)));

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

        index = requirement.indexOf("weapon=", index) + 7;
        Integer tempEndIndex = requirement.indexOf("}", index);
        if (tempEndIndex == -1) {
            tempEndIndex = requirement.length();
        }
        String killerString = requirement.substring(index, tempEndIndex);
        if ("null".equals(killerString)) {
            this.weapon = null;
        } else {
            this.weapon = ParsingUtil.parseString(requirement.substring(index + 1, tempEndIndex - 1));
        }
    }

    @Override
    public boolean updateStatus(HashMap<String, Object> status) {
        if (status.get(AchievementsPresenter.WHAT_KEY) instanceof String && what.equalsIgnoreCase((String) status.get(AchievementsPresenter.WHAT_KEY))
            && (where == null || ((status.get(AchievementsPresenter.WHERE_KEY) instanceof Position && where.equals(status.get(AchievementsPresenter.WHERE_KEY)))))
            && (weapon == null || ((status.get(AchievementsPresenter.WEAPON_KEY) instanceof String && weapon.equalsIgnoreCase((String) status.get(AchievementsPresenter.WEAPON_KEY)))))) {
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
        return "KillRequirement{" +
                "achievementId=" + getAchievementId() +
                ", type=" + getType() +
                ", what='" + what + '\'' +
                ", amount=" + amount +
                ", playerAmount=" + playerAmount +
                ", where=" + where +
                ", weapon='" + weapon + '\'' +
                '}';
    }

    public String getWhat() {
        return what;
    }

    public void setWhat(String what) {
        this.what = what;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getPlayerAmount() {
        return playerAmount;
    }

    public void setPlayerAmount(Integer playerAmount) {
        this.playerAmount = playerAmount;
    }

    public Position getWhere() {
        return where;
    }

    public void setWhere(Position where) {
        this.where = where;
    }

    public String getWeapon() {
        return weapon;
    }

    public void setWeapon(String weapon) {
        this.weapon = weapon;
    }
}
