package com.gmail.risterral.listeners;

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
        pm.registerEvents(new DeathListener(plugin), plugin);
        pm.registerEvents(new BlockPlaceListener(plugin), plugin);
        pm.registerEvents(new PlayerListener(plugin), plugin);
    }
}
