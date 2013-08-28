package com.gmail.risterral.Player;

public class PlayerDto {
    private boolean isAcceptingTrades;
    private boolean isTrading;
    private boolean isBusy;
    private int numberOfPossibleTradeRows;
    int[] slots;

    public PlayerDto() {
        this.isAcceptingTrades = true;
        this.isTrading = false;
        this.isBusy = false;
        this.numberOfPossibleTradeRows = 4;
    }

    public boolean isAcceptingTrades() {
        return isAcceptingTrades;
    }

    public void setAcceptingTrades(boolean acceptingTrades) {
        isAcceptingTrades = acceptingTrades;
    }

    public boolean isTrading() {
        return isTrading;
    }

    public void setTrading(boolean trading) {
        isTrading = trading;
    }

    public boolean isBusy() {
        return isBusy;
    }

    public void setBusy(boolean busy) {
        isBusy = busy;
    }

    public int getNumberOfPossibleTradeRows() {
        return numberOfPossibleTradeRows;
    }

    public void setNumberOfPossibleTradeRows(int numberOfPossibleTradeRows) {
        this.numberOfPossibleTradeRows = numberOfPossibleTradeRows;
    }

    public int[] getSlots() {
        return slots;
    }

    public void setSlots(int[] slots) {
        this.slots = slots;
    }
}
