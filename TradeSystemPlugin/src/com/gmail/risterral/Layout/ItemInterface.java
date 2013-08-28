package com.gmail.risterral.Layout;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class ItemInterface implements InventoryHolder {
    Inventory inventory;
    TradeItemsContainerLayout layout;

    public ItemInterface(String nameOtherPlayer, TradeItemsContainerLayout layout) {
        this.inventory = Bukkit.getServer().createInventory(this, layout.getSize(), generateTitle(nameOtherPlayer));
        this.layout = layout;
        layout.fillInventory(inventory);
    }

    private TradeItemsContainerLayout getLayout() {
        return this.layout;
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    public void accept() {
        for (int slot : this.layout.getAcceptSlots()) {
            this.inventory.setItem(slot, getLayout().getAcceptedItem());
        }
    }

    public void deny() {
        for (int slot : getLayout().getAcceptSlots()) {
            this.inventory.setItem(slot, getLayout().getAcceptItem());
        }
    }

    public void denyTrade() {
        for (int slot : getLayout().getAcceptSlots()) {
//            this.inventory.clear(slot);
//            this.inventory.remove(slot);
            this.inventory.setItem(slot, getLayout().getAcceptItem());
            this.inventory.setItem(slot, getLayout().getAcceptItem());
        }
        for (int slot : getLayout().getStatusSlots()) {
//            this.inventory.remove(slot);
            this.inventory.setItem(slot, getLayout().getAcceptItem());
            this.inventory.setItem(slot, getLayout().getAcceptItem());
        }
    }

    public void acceptOther() {
        ItemStack item = getLayout().getAcceptedItem();
        for (int slot : getLayout().getStatusSlots()) this.inventory.setItem(slot, item);
    }

    public void denyOther() {
        for (int slot : getLayout().getStatusSlots()) this.inventory.setItem(slot, getLayout().getAcceptItem());
    }

    public ItemStack[] getItems(Side side) {
        ItemStack[] items = new ItemStack[getLayout().getRows() * 4 - 1];
        switch (side) {
            case LEFT:
                for (int i = 0; i < getLayout().getLeftSlots().length; i++)
                    items[i] = this.inventory.getItem(getLayout().getLeftSlots()[i]);
                break;
            case RIGHT:
                for (int i = 0; i < getLayout().getRightSlots().length; i++)
                    items[i] = this.inventory.getItem(getLayout().getRightSlots()[i]);
        }

        return items;
    }

    public int getFirstFreeSlot() {
        for (int i = 0; i < getLayout().getLeftSlots().length; i++)
            if (null == this.inventory.getItem(getLayout().getLeftSlots()[i]))
                return i;
        return -1;
    }

    public int getSize() {
        return getLayout().getRows() * 9;
    }

    public boolean isAcceptSlot(int slot) {
        for (int accept : getLayout().getAcceptSlots()) if (accept == slot) return true;
        return false;
    }

    public boolean isRefuseSlot(int slot) {
        for (int refuse : getLayout().getRefuseSlots()) if (refuse == slot) return true;
        return false;
    }

    public int getInventoryIndex(int index, Side side) {
        switch (side) {
            case LEFT:
                return getLayout().getLeftSlots()[index];
            case RIGHT:
                return getLayout().getRightSlots()[index];
        }
        return -1;
    }

    public int getTradeIndex(int index, Side side) {
        if (side.equals(Side.LEFT)) {
            for (int i = 0; i < getLayout().getLeftSlots().length; i++)
                if (getLayout().getLeftSlots()[i] == index) return i;
        }
        if (side.equals(Side.RIGHT)) {
            for (int i = 0; i < getLayout().getRightSlots().length; i++)
                if (getLayout().getRightSlots()[i] == index) return i;
        }
        return -1;
    }

    public void setTradeItem(int index, ItemStack item, Side side) {
        this.inventory.setItem(getInventoryIndex(index, side), item);
    }

    public static String generateTitle(String playerName) {
        String title = "     You";
        while (title.length() + playerName.length() < 32) {
            title = title + " ";
        }
        return title += playerName;
    }

    public static void replaceNullItems(ItemStack[] items) {
        for (int i = 0; i < items.length; i++) if (items[i] == null) items[i] = new ItemStack(0, 1);
    }

    public static enum Side {
        LEFT,
        RIGHT
    }
}
