package com.gmail.risterral.Listeners;

import com.gmail.risterral.Player.PlayerDto;
import com.gmail.risterral.Player.PlayersPresenter;
import com.gmail.risterral.TradeSystemPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;


public class PlayerListener implements Listener {
    private final Plugin plugin;
    private final PlayersPresenter playersPresenter;

    public PlayerListener(Plugin plugin) {
        this.plugin = plugin;
        this.playersPresenter = ((TradeSystemPlugin) plugin).getPlayersPresenter();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        playersPresenter.getPlayers().put(player.getName(), new PlayerDto());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        playersPresenter.getPlayers().remove(player.getName());
    }

    @EventHandler
    public void onInventoryOpent(InventoryOpenEvent event) {
        ((TradeSystemPlugin) plugin).getPlayersPresenter().getPlayers().get(event.getPlayer().getName()).setBusy(true);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (((TradeSystemPlugin) plugin).getPlayersPresenter().getPlayers().containsKey(event.getPlayer().getName())) {
            ((TradeSystemPlugin) plugin).getPlayersPresenter().getPlayers().get(event.getPlayer().getName()).setBusy(false);
        }
    }
}
