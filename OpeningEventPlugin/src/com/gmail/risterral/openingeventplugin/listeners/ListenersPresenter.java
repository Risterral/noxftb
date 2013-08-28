package com.gmail.risterral.openingeventplugin.listeners;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ListenersPresenter {

    private JavaPlugin plugin;

    public ListenersPresenter(JavaPlugin plugin) {
        this.plugin = plugin;
        registerListeners();
    }

    private void registerListeners() {
        PluginManager pm = plugin.getServer().getPluginManager();
        pm.registerEvents(new com.gmail.risterral.openingeventplugin.listeners.InventoryListener(plugin), plugin);
    }
}
