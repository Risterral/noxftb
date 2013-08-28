package com.gmail.risterral.openingeventplugin;

import com.gmail.risterral.openingeventplugin.commands.CommandsPresenter;
import com.gmail.risterral.openingeventplugin.database.DatabasePresenter;
import com.gmail.risterral.openingeventplugin.listeners.ListenersPresenter;
import com.gmail.risterral.openingeventplugin.openingevent.OpeningEventsPresenter;
import org.bukkit.plugin.java.JavaPlugin;

public final class OpeningEventPlugin extends JavaPlugin {

    private DatabasePresenter databasePresenter;
    private OpeningEventsPresenter openingEventsPresenter;
    private ListenersPresenter listenersPresenter;
    private CommandsPresenter commandsPresenter;

    @Override
    public void onEnable() {
        databasePresenter = new DatabasePresenter(this);
        openingEventsPresenter = new OpeningEventsPresenter(this);
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

    public OpeningEventsPresenter getOpeningEventsPresenter() {
        return openingEventsPresenter;
    }

    public ListenersPresenter getListenersPresenter() {
        return listenersPresenter;
    }

    public CommandsPresenter getCommandsPresenter() {
        return commandsPresenter;
    }
}
