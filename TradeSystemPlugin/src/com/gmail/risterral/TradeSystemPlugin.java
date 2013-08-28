package com.gmail.risterral;

import com.gmail.risterral.Commands.CommandsPresenter;
import com.gmail.risterral.Exchange.ExchangesPresenter;
import com.gmail.risterral.Listeners.ListenersPresenter;
import com.gmail.risterral.Player.PlayersPresenter;
import org.bukkit.plugin.java.JavaPlugin;

public final class TradeSystemPlugin extends JavaPlugin {
    private CommandsPresenter commandsPresenter;
    private ListenersPresenter listenersPresenter;
    private PlayersPresenter playersPresenter;
    private ExchangesPresenter exchangesPresenter;

    @Override
    public void onEnable() {
        playersPresenter = new PlayersPresenter();
        commandsPresenter = new CommandsPresenter(this);
        exchangesPresenter = new ExchangesPresenter(this);
        listenersPresenter = new ListenersPresenter(this);
    }

    @Override
    public void onDisable() {
        this.exchangesPresenter.stopAll();
    }

    public PlayersPresenter getPlayersPresenter() {
        return playersPresenter;
    }

    public ExchangesPresenter getExchangesPresenter() {
        return exchangesPresenter;
    }
}
