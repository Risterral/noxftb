package com.gmail.risterral.Listeners;

import com.gmail.risterral.Exchange.Exchange;
import com.gmail.risterral.Exchange.ExchangesPresenter;
import com.gmail.risterral.Layout.ItemInterface;
import com.gmail.risterral.Player.PlayerDto;
import com.gmail.risterral.Player.PlayersPresenter;
import com.gmail.risterral.TradeSystemPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class TradeListener implements Listener {
    TradeSystemPlugin plugin;
    ExchangesPresenter exchangesPresenter;
    private PlayersPresenter playersPresenter;

    public TradeListener(TradeSystemPlugin instance) {
        this.plugin = instance;
        this.exchangesPresenter = this.plugin.getExchangesPresenter();
        this.playersPresenter = this.plugin.getPlayersPresenter();
    }

    @EventHandler
    public void onPlayerAbandon(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        Exchange exchange = this.exchangesPresenter.getActiveExchanges().get(player);
        if (exchange != null) {
            exchange.refuse(player);
        }
    }

    @EventHandler
    public void onTradeInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        PlayerDto playerDto = playersPresenter.getPlayers().get(player.getName());
        if (!playerDto.isTrading()) return;

        Exchange exchange = this.exchangesPresenter.getActiveExchanges().get(player);

        if (event.isShiftClick()) {
            cancelInventoryClickEvent(event);
            return;
        }

        int inventorySlot = event.getRawSlot();

        if (inventorySlot == -1) return;

        ItemInterface itemInterface = exchange.getInterface(player);

        if (inventorySlot > itemInterface.getSize() - 1) return;

        ItemStack cursor = event.getCursor();

        ItemStack currentSlot = event.getCurrentItem();

        if (!isExchangeContainingEvent(exchange, player.getName())) {
            exchange.getPlayerEvents().add(new InventoryClickEvent(event.getView(), event.getSlotType(), 0, false, false));
        }

        if (itemInterface.isAcceptSlot(inventorySlot)) {
            if (exchange.hasAccepted(player)) {
                exchange.denyTrade(player);
            } else {
                if (exchange.getFakeAccept() > 0) {
                    exchange.fakeAcceptTrade(player);
                    exchange.setFakeAccept(exchange.getFakeAccept() - 1);
                } else {
                    exchange.acceptTrade(player);
                }
            }
            cancelInventoryClickEvent(event);
            return;
        }

        if (itemInterface.isRefuseSlot(inventorySlot)) {
            exchange.refuse(player);
            cancelInventoryClickEvent(event);
            return;
        }

        int tradingSlotIndex = itemInterface.getTradeIndex(inventorySlot, ItemInterface.Side.LEFT);

        if (tradingSlotIndex == -1) {
            cancelInventoryClickEvent(event);
            return;
        }

        ItemStack newSlot = null;

        if ((currentSlot.getTypeId() == 0) && (cursor.getTypeId() == 0)) return;


        if (event.isLeftClick()) {
            if (currentSlot.getTypeId() == cursor.getTypeId()) {
                newSlot = currentSlot.clone();
                newSlot.setAmount(currentSlot.getAmount() + cursor.getAmount());
            } else {
                newSlot = cursor.clone();
            }
        }

        if (event.isRightClick()) {
            if ((currentSlot.getTypeId() != 0) && (cursor.getTypeId() == 0)) {
                newSlot = currentSlot.clone();
                newSlot.setAmount(currentSlot.getAmount() / 2);
            }

            if ((currentSlot.getTypeId() != cursor.getTypeId()) && (cursor.getTypeId() != 0)) {
                newSlot = cursor.clone();
            }

            if ((currentSlot.getTypeId() == cursor.getTypeId()) && (cursor.getTypeId() != 0)) {
                newSlot = currentSlot.clone();
                newSlot.setAmount(currentSlot.getAmount() + 1);
            }

            if ((currentSlot.getTypeId() == 0) && (cursor.getTypeId() != 0)) {
                newSlot = cursor.clone();
                newSlot.setAmount(1);
            }

        }

        ItemInterface otherInterface = exchange.getInterface(exchange.getOtherPlayer(player));
        otherInterface.setTradeItem(tradingSlotIndex, newSlot, ItemInterface.Side.RIGHT);
        exchange.cancelAcceptOf();

        for (InventoryClickEvent exchangeEvent : exchange.getPlayerEvents()) {
            exchange.setFakeAccept(exchange.getFakeAccept() + 1);
            BukkitTask task = new AcceptClickTask(exchangeEvent).runTaskLater(plugin, 1);
        }
    }

    private void cancelInventoryClickEvent(InventoryClickEvent event) {
        event.setCursor(event.getCursor());
        event.setCurrentItem(event.getCurrentItem());
        event.setCancelled(true);
        event.setResult(Event.Result.DENY);
    }

    private boolean isExchangeContainingEvent(Exchange exchange, String playerName) {
        for (InventoryClickEvent exchangeEvent : exchange.getPlayerEvents()) {
            if (exchangeEvent.getWhoClicked().getName().equals(playerName)) {
                return true;
            }
        }
        return false;
    }

    public class AcceptClickTask extends BukkitRunnable {

        private InventoryClickEvent eventToRun;


        public AcceptClickTask(InventoryClickEvent eventToRun) {
            this.eventToRun = eventToRun;
        }

        public void run() {
            Bukkit.getServer().getPluginManager().callEvent(eventToRun);
            Bukkit.getServer().getPluginManager().callEvent(eventToRun);
        }

    }
}
