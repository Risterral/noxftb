package com.gmail.risterral.Listeners;

import com.gmail.risterral.TradeSystemPlugin;
import org.bukkit.plugin.PluginManager;

public class ListenersPresenter {

    private TradeSystemPlugin plugin;

    public ListenersPresenter(TradeSystemPlugin plugin) {
        this.plugin = plugin;
        registerListeners();
    }

    private void registerListeners() {
        PluginManager pm = plugin.getServer().getPluginManager();
        pm.registerEvents(new PlayerListener(plugin), plugin);
        pm.registerEvents(new TradeListener(plugin), plugin);
    }
}
