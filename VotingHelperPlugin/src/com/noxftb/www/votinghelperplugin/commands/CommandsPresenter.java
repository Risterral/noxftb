package com.noxftb.www.votinghelperplugin.commands;

import org.bukkit.plugin.java.JavaPlugin;

public class CommandsPresenter {

    private JavaPlugin plugin;

    public CommandsPresenter(JavaPlugin plugin) {
        this.plugin = plugin;
        registerCommands();
    }

    public void registerCommands() {

        //TODO: template
//        plugin.getCommand("register").setExecutor(new RegisterCommand(plugin));
    }
}
