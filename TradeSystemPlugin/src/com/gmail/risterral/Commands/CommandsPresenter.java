package com.gmail.risterral.Commands;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandsPresenter {

    private Plugin plugin;

    public CommandsPresenter(Plugin plugin) {
        this.plugin = plugin;
        registerCommands();
    }

    public void registerCommands() {
        ((JavaPlugin) plugin).getCommand("trade").setExecutor(new TradeCommand(plugin));
        ((JavaPlugin) plugin).getCommand("accept").setExecutor(new AcceptTradesCommand(plugin));
        ((JavaPlugin) plugin).getCommand("reject").setExecutor(new RejectTradesCommand(plugin));
    }
}
