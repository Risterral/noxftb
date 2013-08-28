package com.gmail.risterral.commands;

import com.gmail.risterral.commands.notoped.AchievementCommand;
import com.gmail.risterral.commands.notoped.AchievementsCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandsPresenter {

    private JavaPlugin plugin;

    public CommandsPresenter(JavaPlugin plugin) {
        this.plugin = plugin;
        registerCommands();
    }

    public void registerCommands() {

        plugin.getCommand("achievements").setExecutor(new AchievementsCommand());
        plugin.getCommand("achievement").setExecutor(new AchievementCommand(plugin));
        plugin.getCommand("pos").setExecutor(new SamplePosCommand(plugin));
    }
}
