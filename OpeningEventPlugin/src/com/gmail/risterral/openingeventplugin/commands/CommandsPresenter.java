package com.gmail.risterral.openingeventplugin.commands;

import org.bukkit.plugin.java.JavaPlugin;

public class CommandsPresenter {

    private JavaPlugin plugin;

    public CommandsPresenter(JavaPlugin plugin) {
        this.plugin = plugin;
        registerCommands();
    }

    public void registerCommands() {

        plugin.getCommand("register").setExecutor(new RegisterCommand(plugin));
        plugin.getCommand("openingevent").setExecutor(new OpeningEventCommand(plugin));
        plugin.getCommand("unregister").setExecutor(new UnregisterCommand(plugin));
    }
}
