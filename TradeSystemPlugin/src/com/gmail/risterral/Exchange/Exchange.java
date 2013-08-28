package com.gmail.risterral.Exchange;

import com.gmail.risterral.Layout.ItemInterface;
import com.gmail.risterral.Layout.TradeItemsContainerLayout;
import com.gmail.risterral.Player.PlayerDto;
import com.gmail.risterral.Player.PlayersPresenter;
import com.gmail.risterral.TradeSystemPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class Exchange {
    protected final TradeSystemPlugin plugin;
    private final PlayersPresenter playersPresenter;
    protected final Player player1;
    protected final Player player2;
    protected final PlayerDto player1Dto;
    protected final PlayerDto player2Dto;
    TradeItemsContainerLayout layout;
    protected int possibleTradeRows = 0;
    ItemInterface player1gui;
    ItemInterface player2gui;
    boolean isPlayer1Accepted = false;
    boolean isPlayer2Accepted = false;
    List<InventoryClickEvent> playerEvents = new ArrayList<InventoryClickEvent>();
    int fakeAccept = 0;

    public Exchange(TradeSystemPlugin plugin, Player player1, Player player2) {
        this.plugin = plugin;
        this.playersPresenter = plugin.getPlayersPresenter();
        this.player1 = player1;
        this.player2 = player2;
        this.player1Dto = playersPresenter.getPlayers().get(player1.getName());
        this.player2Dto = playersPresenter.getPlayers().get(player2.getName());

        this.initialize();
        this.start();
    }

    private void initialize() {
        setRows();

        this.layout = new TradeItemsContainerLayout(this.possibleTradeRows);

        this.player1gui = new ItemInterface(this.player2.getName(), this.layout);
        this.player2gui = new ItemInterface(this.player1.getName(), this.layout);
        this.player1Dto.setSlots(this.layout.getLeftSlots());
        this.player2Dto.setSlots(this.layout.getLeftSlots());
        this.player1Dto.setTrading(true);
        this.player2Dto.setTrading(true);
    }

    protected void setRows() {
        int rowsP1 = this.player1Dto.getNumberOfPossibleTradeRows();
        int rowsP2 = this.player2Dto.getNumberOfPossibleTradeRows();

        if (rowsP2 > rowsP1)
            this.possibleTradeRows = rowsP2;
        else
            this.possibleTradeRows = rowsP1;
    }

    public void acceptTrade(Player who) {
        if (who.equals(this.player1)) {
            this.isPlayer1Accepted = true;
            getInterface(this.player1).accept();
            getInterface(this.player2).acceptOther();
            player1.playSound(player1.getLocation(), Sound.NOTE_PLING, 1, 0);
        } else if (who.equals(this.player2)) {
            this.isPlayer2Accepted = true;
            getInterface(this.player2).accept();
            getInterface(this.player1).acceptOther();
            player2.playSound(player2.getLocation(), Sound.NOTE_PLING, 1, 0);
        } else {
            return;
        }

        if ((this.isPlayer1Accepted) && (this.isPlayer2Accepted)) finish(false);
    }

    public void fakeAcceptTrade(Player who) {
        if (who.equals(this.player1)) {
            this.isPlayer1Accepted = true;
            getInterface(this.player1).accept();
            getInterface(this.player2).acceptOther();
        } else if (who.equals(this.player2)) {
            this.isPlayer2Accepted = true;
            getInterface(this.player2).accept();
            getInterface(this.player1).acceptOther();
        } else {
            return;
        }
    }

    public void denyTrade(Player who) {
        if ((who.equals(this.player1)) && (this.isPlayer1Accepted)) {
            this.isPlayer1Accepted = false;

            getInterface(this.player1).deny();
            getInterface(this.player2).denyOther();

            return;
        }

        if ((who.equals(this.player2)) && (this.isPlayer2Accepted)) {
            this.isPlayer2Accepted = false;

            getInterface(this.player2).deny();
            getInterface(this.player1).denyOther();

            return;
        }
    }

    public void cancelAcceptOf() {
        this.isPlayer1Accepted = false;
        this.isPlayer2Accepted = false;
    }

    public void refuse(Player player) {
        player.sendRawMessage(ChatColor.RED + "Trade cancelled.");
        getOtherPlayer(player).sendRawMessage(ChatColor.RED + player.getName() + " has cancelled the trade.");
        finish(true);
    }

    public boolean hasAccepted(Player player) {
        if (player.equals(this.player1))
            return this.isPlayer1Accepted;
        if (player.equals(this.player2)) {
            return this.isPlayer2Accepted;
        }
        return false;
    }

    public Player getOtherPlayer(Player player) {
        if (player.equals(this.player1)) return this.player2;
        if (player.equals(this.player2)) return this.player1;
        return null;
    }

    public ItemInterface getInterface(Player player) {
        if (player.equals(this.player1)) return this.player1gui;
        if (player.equals(this.player2)) return this.player2gui;
        return null;
    }

    private void start() {
        if ((getInterface(this.player1) == null) || (getInterface(this.player2) == null)) return;

        this.player1.openInventory(getInterface(this.player1).getInventory());
        this.player2.openInventory(getInterface(this.player2).getInventory());
    }

    public void finish(boolean forceStop) {
        BukkitTask taskPlayer1 = new GiveItemBack(player1, this.player1.getItemOnCursor()).runTaskLater(plugin, 1);
        this.player1.setItemOnCursor(new ItemStack(0));
        BukkitTask taskPlayer2 = new GiveItemBack(player2, this.player2.getItemOnCursor()).runTaskLater(plugin, 1);
        this.player2.setItemOnCursor(new ItemStack(0));

        if (forceStop) {
            revertOffers();
        } else if (!isTradePossibleDueToSpace()) {
            player1.sendRawMessage(ChatColor.RED + "Trade canceled due to not enough free space.");
            player2.sendRawMessage(ChatColor.RED + "Trade canceled due to not enough free space.");
            revertOffers();
        } else {
            giveOffers();
            this.player1.sendRawMessage(ChatColor.GREEN + "Trade successful.");
            this.player2.sendRawMessage(ChatColor.GREEN + "Trade successful.");
            this.player1.playSound(player1.getLocation(), Sound.ORB_PICKUP, 1, 0);
            this.player2.playSound(player1.getLocation(), Sound.ORB_PICKUP, 1, 0);
        }


        this.plugin.getExchangesPresenter().getActiveExchanges().remove(this.player1);
        this.plugin.getExchangesPresenter().getActiveExchanges().remove(this.player2);
        this.player1.closeInventory();
        this.player2.closeInventory();
        this.player1Dto.setTrading(false);
        this.player2Dto.setTrading(false);
        this.player1Dto.setBusy(false);
        this.player2Dto.setBusy(false);
    }

    public boolean isTradePossibleDueToSpace() {
        int freeSpacePlayer1 = 0;
        for (ItemStack itemStack : player1.getInventory().getContents()) {
            if (itemStack == null) {
                freeSpacePlayer1++;
            }
        }
        int itemsToTrade = 0;
        for (ItemStack itemStack : getInterface(this.player1).getItems(ItemInterface.Side.RIGHT)) {
            if (null != itemStack) {
                itemsToTrade++;
            }
        }
        if (freeSpacePlayer1 < itemsToTrade) {
            return false;
        }

        int freeSpacePlayer2 = 0;
        for (ItemStack itemStack : player2.getInventory().getContents()) {
            if (itemStack == null) {
                freeSpacePlayer2++;
            }
        }
        itemsToTrade = 0;
        for (ItemStack itemStack : getInterface(this.player2).getItems(ItemInterface.Side.RIGHT)) {
            if (null != itemStack) {
                itemsToTrade++;
            }
        }
        if (freeSpacePlayer2 < itemsToTrade) {
            return false;
        }
        return true;
    }

    protected void giveOffers() {
        ItemStack[] player1items = getInterface(this.player1).getItems(ItemInterface.Side.RIGHT);
        ItemInterface.replaceNullItems(player1items);
        this.player1.getInventory().addItem(player1items);

        ItemStack[] player2items = getInterface(this.player2).getItems(ItemInterface.Side.RIGHT);
        ItemInterface.replaceNullItems(player2items);
        this.player2.getInventory().addItem(player2items);
    }

    protected void revertOffers() {
        ItemStack[] player1items = getInterface(this.player1).getItems(ItemInterface.Side.LEFT);
        ItemInterface.replaceNullItems(player1items);
        this.player1.getInventory().addItem(player1items);

        ItemStack[] player2items = getInterface(this.player2).getItems(ItemInterface.Side.LEFT);
        ItemInterface.replaceNullItems(player2items);
        this.player2.getInventory().addItem(player2items);
    }

    public List<InventoryClickEvent> getPlayerEvents() {
        return playerEvents;
    }

    public int getFakeAccept() {
        return fakeAccept;
    }

    public void setFakeAccept(int fakeAccept) {
        this.fakeAccept = fakeAccept;
    }

    public class GiveItemBack extends BukkitRunnable {
        private Player player;
        private ItemStack item;

        public GiveItemBack(Player player, ItemStack item) {
            this.player = player;
            this.item = item;
        }

        public void run() {
            this.player.getInventory().addItem(item);
        }
    }
}
