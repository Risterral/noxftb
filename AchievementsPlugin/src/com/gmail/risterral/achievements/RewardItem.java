package com.gmail.risterral.achievements;

public class RewardItem {

    private Integer id;
    private Integer amount;
    private String description;
    private String lore;


    public RewardItem() {
    }

    public RewardItem(String rewardItem) {
        Integer index = rewardItem.indexOf("id=") + 3;
        this.id = Integer.parseInt(rewardItem.substring(index, rewardItem.indexOf(",", index)));

        index = rewardItem.indexOf("amount=", index) + 7;
        this.amount = Integer.parseInt(rewardItem.substring(index, rewardItem.indexOf(",", index)));

        index = rewardItem.indexOf("description=", index) + 12;
        String value = rewardItem.substring(index, rewardItem.indexOf(",", index));
        if (!"null".equals(value)) {
            this.description = rewardItem.substring(index + 1, rewardItem.indexOf("',", index));
        }

        index = rewardItem.indexOf("lore=", index) + 5;
        Integer tempEndIndex = rewardItem.indexOf("}", index);
        if (tempEndIndex == -1) {
            tempEndIndex = rewardItem.length();
        }
        value = rewardItem.substring(index, tempEndIndex);
        if (!"null".equals(value)) {
            this.lore = rewardItem.substring(index + 1, tempEndIndex - 1);
        }
    }

    @Override
    public String toString() {
        return "RewardItem{" +
                "id=" + id +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", lore='" + lore + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RewardItem that = (RewardItem) o;

        if (amount != null ? !amount.equals(that.amount) : that.amount != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (lore != null ? !lore.equals(that.lore) : that.lore != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (lore != null ? lore.hashCode() : 0);
        return result;
    }

    public Integer getId() {

        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLore() {
        return lore;
    }

    public void setLore(String lore) {
        this.lore = lore;
    }
}
