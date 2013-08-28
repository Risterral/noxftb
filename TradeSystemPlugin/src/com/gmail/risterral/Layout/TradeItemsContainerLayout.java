package com.gmail.risterral.Layout;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TradeItemsContainerLayout {
    int[] leftSlots;
    int[] rightSlots;
    int[] separatorSlots;
    int[] acceptSlots;
    int[] refuseSlots;
    int[] statusSlots;
    ItemStack acceptItem;
    ItemStack acceptedItem;
    ItemStack refuseItem;
    ItemStack pendingItem;
    ItemStack separatorItem;
    private int rows;

    public TradeItemsContainerLayout(int rows) {
        this.rows = rows;
        this.setActionSlots();
        this.setTradeSlots();
        this.setActionItems();
    }

    public int getSize() {
        return this.rows * 9;
    }

    public int getRows() {
        return this.rows;
    }

    public void fillInventory(Inventory inventory) {
        for (int slot : getSeparatorSlots()) inventory.setItem(slot, getSeparatorItem());
        for (int slot : getAcceptSlots()) inventory.setItem(slot, getAcceptItem());
        for (int slot : getRefuseSlots()) inventory.setItem(slot, getRefuseItem());
        for (int slot : getStatusSlots()) inventory.setItem(slot, getPendingItem());
    }

    public int[] getAcceptSlots() {
        return this.acceptSlots;
    }

    public int[] getRefuseSlots() {
        return this.refuseSlots;
    }

    public int[] getStatusSlots() {
        return this.statusSlots;
    }

    public int[] getLeftSlots() {
        return this.leftSlots;
    }

    public int[] getRightSlots() {
        return this.rightSlots;
    }

    public int[] getSeparatorSlots() {
        return this.separatorSlots;
    }

    public ItemStack getAcceptItem() {
        return this.acceptItem.clone();
    }

    public ItemStack getAcceptedItem() {
        return this.acceptedItem.clone();
    }

    public ItemStack getRefuseItem() {
        return this.refuseItem.clone();
    }

    public ItemStack getPendingItem() {
        return this.pendingItem.clone();
    }

    public ItemStack getSeparatorItem() {
        return this.separatorItem.clone();
    }

    private void setActionSlots() {
        this.acceptSlots = new int[]{0};
        this.refuseSlots = new int[]{4};
        this.statusSlots = new int[]{8};

        int separatorRows = this.rows - 1;

        this.separatorSlots = new int[this.rows - 1];
        for (int i = 0; i < separatorRows; i++) {
            this.separatorSlots[i] = 13 + i * 9;
        }
    }

    private void setTradeSlots() {
        switch (this.rows) {
            case 1:
                this.leftSlots = new int[]{1, 2, 3};
                this.rightSlots = new int[]{5, 6, 7};
                break;
            case 2:
                this.leftSlots = new int[]{1, 2, 3, 9, 10, 11, 12};
                this.rightSlots = new int[]{5, 6, 7, 14, 15, 16, 17};
                break;
            case 3:
                this.leftSlots = new int[]{1, 2, 3, 9, 10, 11, 12, 18, 19, 20, 21};
                this.rightSlots = new int[]{5, 6, 7, 14, 15, 16, 17, 23, 24, 25, 26};
                break;
            case 4:
                this.leftSlots = new int[]{1, 2, 3, 9, 10, 11, 12, 18, 19, 20, 21, 27, 28, 29, 30};
                this.rightSlots = new int[]{5, 6, 7, 14, 15, 16, 17, 23, 24, 25, 26, 32, 33, 34, 35};
                break;
            case 5:
                this.leftSlots = new int[]{1, 2, 3, 9, 10, 11, 12, 18, 19, 20, 21, 27, 28, 29, 30, 36, 37, 38, 39};
                this.rightSlots = new int[]{5, 6, 7, 14, 15, 16, 17, 23, 24, 25, 26, 32, 33, 34, 35, 41, 42, 43, 44};
                break;
            case 6:
                this.leftSlots = new int[]{1, 2, 3, 9, 10, 11, 12, 18, 19, 20, 21, 27, 28, 29, 30, 36, 37, 38, 39, 45, 46, 47, 48};
                this.rightSlots = new int[]{5, 6, 7, 14, 15, 16, 17, 23, 24, 25, 26, 32, 33, 34, 35, 41, 42, 43, 44, 50, 51, 52, 53};
                break;
        }
    }

    private void setActionItems() {
        ItemStack acceptItem = new ItemStack(35, 0, (short) 8);
        ItemMeta acceptMeta = acceptItem.getItemMeta();
        acceptMeta.setDisplayName("Click here to accept trade");
        acceptItem.setItemMeta(acceptMeta);

        ItemStack acceptedItem = new ItemStack(35, 0, (short) 5);
        ItemMeta acceptedMeta = acceptedItem.getItemMeta();
        acceptedMeta.setDisplayName("Click here to resume trade");
        acceptedItem.setItemMeta(acceptedMeta);

        ItemStack refuseItem = new ItemStack(35, 0, (short) 14);
        ItemMeta refuseMeta = refuseItem.getItemMeta();
        refuseMeta.setDisplayName("Click here to cancel trade");
        refuseItem.setItemMeta(refuseMeta);

        ItemStack pendingItem = new ItemStack(35, 0, (short) 8);
        ItemMeta pendingMeta = pendingItem.getItemMeta();
        pendingMeta.setDisplayName("Trading in progress");
        pendingItem.setItemMeta(pendingMeta);

        ItemStack separatorItem = new ItemStack(104, 0);
        ItemMeta separatorMeta = refuseItem.getItemMeta();
        separatorMeta.setDisplayName("Separator");
        separatorItem.setItemMeta(separatorMeta);

        this.acceptItem = acceptItem;
        this.acceptedItem = acceptedItem;
        this.refuseItem = refuseItem;
        this.pendingItem = pendingItem;
        this.separatorItem = separatorItem;
    }

    public void setDefaultMetaInfo() {
        ItemStack acceptItem = new ItemStack(35, 0, (short) 8);
        ItemMeta acceptMeta = acceptItem.getItemMeta();
        acceptMeta.setDisplayName("Light Gray Wool");
        acceptItem.setItemMeta(acceptMeta);

        ItemStack acceptedItem = new ItemStack(35, 0, (short) 5);
        ItemMeta acceptedMeta = acceptedItem.getItemMeta();
        acceptedMeta.setDisplayName("Light Green Wool");
        acceptedItem.setItemMeta(acceptedMeta);

        ItemStack refuseItem = new ItemStack(35, 0, (short) 14);
        ItemMeta refuseMeta = refuseItem.getItemMeta();
        refuseMeta.setDisplayName("Red Wool");
        refuseItem.setItemMeta(refuseMeta);

        ItemStack pendingItem = new ItemStack(35, 0, (short) 8);
        ItemMeta pendingMeta = pendingItem.getItemMeta();
        pendingMeta.setDisplayName("Light Gray Wool");
        pendingItem.setItemMeta(pendingMeta);

        ItemStack separatorItem = new ItemStack(104, 0);
        ItemMeta separatorMeta = refuseItem.getItemMeta();
        separatorMeta.setDisplayName("Pumpkin Stem");
        separatorItem.setItemMeta(separatorMeta);
    }
}
