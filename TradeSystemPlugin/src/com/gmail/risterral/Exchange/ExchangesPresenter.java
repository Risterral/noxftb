package com.gmail.risterral.Exchange;

import com.gmail.risterral.TradeSystemPlugin;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExchangesPresenter {
    private final TradeSystemPlugin plugin;
    private Map<Player, Exchange> activeExchanges = new HashMap<Player, Exchange>();

    public ExchangesPresenter(TradeSystemPlugin instance) {
        this.plugin = instance;
    }

    public void start(Player requested, Player requester) {
        Exchange exchange = new Exchange(this.plugin, requester, requested);
        this.activeExchanges.put(requested, exchange);
        this.activeExchanges.put(requester, exchange);
    }

    public Map<Player, Exchange> getActiveExchanges() {
        return this.activeExchanges;
    }

    public void stopAll() {
        List<Exchange> exchanges = new ArrayList<Exchange>(this.activeExchanges.values());
        for (Exchange exchange : exchanges)
            exchange.finish(true);
    }
}
