package com.noxftb.www.votinghelperplugin;

import com.noxftb.www.votinghelperplugin.commands.CommandsPresenter;
import com.noxftb.www.votinghelperplugin.database.DatabasePresenter;
import com.noxftb.www.votinghelperplugin.listeners.ListenersPresenter;
import org.bukkit.plugin.java.JavaPlugin;

public final class VotingHelperPlugin extends JavaPlugin {

    private DatabasePresenter databasePresenter;
    private ListenersPresenter listenersPresenter;
    private CommandsPresenter commandsPresenter;

    @Override
    public void onEnable() {
        databasePresenter = new DatabasePresenter(this);
        listenersPresenter = new ListenersPresenter(this);
        commandsPresenter = new CommandsPresenter(this);
    }

    @Override
    public void onDisable() {
        databasePresenter.getDatabase().closeConnection();
    }

    public DatabasePresenter getDatabasePresenter() {
        return databasePresenter;
    }

    public ListenersPresenter getListenersPresenter() {
        return listenersPresenter;
    }

    public CommandsPresenter getCommandsPresenter() {
        return commandsPresenter;
    }
}
