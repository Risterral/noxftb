package com.gmail.risterral.openingeventplugin.openingevent;

public class OpeningEventRequirement {
    private String requiredItem;
    private Integer presentAmount;
    private Integer ultimateAmount;
    private Boolean completed;

    public OpeningEventRequirement(String requiredItem, Integer ultimateAmount) {
        this.requiredItem = requiredItem;
        this.presentAmount = 0;
        this.ultimateAmount = ultimateAmount;
        this.completed = false;
    }

    public OpeningEventRequirement(String requiredItem, Integer presentAmount, Integer ultimateAmount, Boolean completed) {
        this.requiredItem = requiredItem;
        this.presentAmount = presentAmount;
        this.ultimateAmount = ultimateAmount;
        this.completed = completed;
    }

    public String getRequiredItem() {
        return requiredItem;
    }

    public void setRequiredItem(String requiredItem) {
        this.requiredItem = requiredItem;
    }

    public Integer getPresentAmount() {
        return presentAmount;
    }

    public void setPresentAmount(Integer presentAmount) {
        this.presentAmount = presentAmount;
    }

    public Integer getUltimateAmount() {
        return ultimateAmount;
    }

    public void setUltimateAmount(Integer ultimateAmount) {
        this.ultimateAmount = ultimateAmount;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }
}
