package com.gmail.risterral.achievements;

import com.gmail.risterral.achievements.requirements.AbstractRequirement;
import com.gmail.risterral.utils.ParsingUtil;

import java.util.ArrayList;
import java.util.List;

public class RewardDto {

    private String message;
    private Integer experience;
    private List<RewardItem> items;


    public RewardDto() {
        items = new ArrayList<RewardItem>();
    }

    public RewardDto(String reward) {
        Integer index = reward.indexOf("message='") + 9;
        this.message = reward.substring(index, reward.indexOf("'", index));

        index = reward.indexOf("experience=", index) + 11;
        this.experience = ParsingUtil.parseInteger(reward.substring(index, reward.indexOf(",", index)));

        index = reward.indexOf("items=[", index) + 7;
        Integer tempEndIndex = reward.indexOf("]", index);
        if (tempEndIndex == -1) {
            tempEndIndex = reward.length();
        }
        String rewardItemsString = reward.substring(index, tempEndIndex);
        this.items = new ArrayList<RewardItem>();
        if (!rewardItemsString.isEmpty()) {
            for (String rewardItemString : rewardItemsString.split("}, RewardItem\\{")) {
                this.items.add(new RewardItem(rewardItemString));
            }
        }
    }

    @Override
    public String toString() {
        return "RewardDto{" +
                "message='" + message + '\'' +
                ", experience=" + experience +
                ", items=" + items +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RewardDto rewardDto = (RewardDto) o;

        if (experience != null ? !experience.equals(rewardDto.experience) : rewardDto.experience != null) return false;
        if (items != null ? !items.equals(rewardDto.items) : rewardDto.items != null) return false;
        if (message != null ? !message.equals(rewardDto.message) : rewardDto.message != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = message != null ? message.hashCode() : 0;
        result = 31 * result + (experience != null ? experience.hashCode() : 0);
        result = 31 * result + (items != null ? items.hashCode() : 0);
        return result;
    }

    public String getMessage() {

        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public List<RewardItem> getItems() {
        return items;
    }

    public void setItems(List<RewardItem> items) {
        this.items = items;
    }
}
