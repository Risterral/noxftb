package com.noxftb.www.votinghelperplugin;

import com.noxftb.www.votinghelperplugin.commands.CommandsPresenter;
import com.noxftb.www.votinghelperplugin.database.DatabasePresenter;
import com.noxftb.www.votinghelperplugin.listeners.ListenersPresenter;
import com.noxftb.www.votinghelperplugin.player.PlayersPresenter;
import com.noxftb.www.votinghelperplugin.voting.VotingPresenter;
import org.bukkit.plugin.java.JavaPlugin;

public final class VotingHelperPlugin extends JavaPlugin {

    private DatabasePresenter databasePresenter;
    private VotingPresenter votingPresenter;
    private PlayersPresenter playersPresenter;
    private ListenersPresenter listenersPresenter;
    private CommandsPresenter commandsPresenter;

    @Override
    public void onEnable() {
        databasePresenter = new DatabasePresenter(this);
        votingPresenter = new VotingPresenter(this);
        playersPresenter = new PlayersPresenter(this);
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

    public VotingPresenter getVotingPresenter() {
        return votingPresenter;
    }

    public PlayersPresenter getPlayersPresenter() {
        return playersPresenter;
    }

    public ListenersPresenter getListenersPresenter() {
        return listenersPresenter;
    }

    public CommandsPresenter getCommandsPresenter() {
        return commandsPresenter;
    }
}
