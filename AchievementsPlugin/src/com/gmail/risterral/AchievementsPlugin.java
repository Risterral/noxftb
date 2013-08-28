package com.gmail.risterral;

import com.gmail.risterral.achievements.AchievementsPresenter;
import com.gmail.risterral.listeners.ListenersPresenter;
import com.gmail.risterral.player.PlayersPresenter;
import com.gmail.risterral.commands.CommandsPresenter;
import com.gmail.risterral.database.DatabasePresenter;
import org.bukkit.plugin.java.JavaPlugin;

public final class AchievementsPlugin extends JavaPlugin {

    private DatabasePresenter databasePresenter;
    private AchievementsPresenter achievementsPresenter;
    private CommandsPresenter commandsPresenter;
    private ListenersPresenter listenersPresenter;
    private PlayersPresenter playersPresenter;

    @Override
    public void onEnable() {
        achievementsPresenter = new AchievementsPresenter(this);
        databasePresenter = new DatabasePresenter(this);
        commandsPresenter = new CommandsPresenter(this);
        listenersPresenter = new ListenersPresenter(this);
        playersPresenter = new PlayersPresenter(this);
    }

    @Override
    public void onDisable() {
        databasePresenter.getDatabase().closeConnection();
    }

    public DatabasePresenter getDatabasePresenter() {
        return databasePresenter;
    }

    public AchievementsPresenter getAchievementsPresenter() {
        return achievementsPresenter;
    }

    public CommandsPresenter getCommandsPresenter() {
        return commandsPresenter;
    }

    public ListenersPresenter getListenersPresenter() {
        return listenersPresenter;
    }

    public PlayersPresenter getPlayersPresenter() {
        return playersPresenter;
    }
}
