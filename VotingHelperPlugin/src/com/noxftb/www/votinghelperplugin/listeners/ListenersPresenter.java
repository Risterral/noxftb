package com.noxftb.www.votinghelperplugin.listeners;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ListenersPresenter {

    private JavaPlugin plugin;

    public ListenersPresenter(JavaPlugin plugin) {
        this.plugin = plugin;
        registerListeners();
    }

    private void registerListeners() {
        PluginManager pluginManager = plugin.getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerListener(plugin), plugin);
    }
}
