package com.noxftb.www.votinghelperplugin.listeners;

import com.noxftb.www.votinghelperplugin.VotingHelperPlugin;
import com.noxftb.www.votinghelperplugin.database.DatabasePresenter;
import com.noxftb.www.votinghelperplugin.player.PlayersPresenter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

public class PlayerListener implements Listener {
    private final PlayersPresenter playersPresenter;
    private final DatabasePresenter databasePresenter;

    public PlayerListener(Plugin plugin) {
        this.playersPresenter = ((VotingHelperPlugin) plugin).getPlayersPresenter();
        this.databasePresenter = ((VotingHelperPlugin) plugin).getDatabasePresenter();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!playersPresenter.getPlayers().containsKey(player.getName())) {
            playersPresenter.getPlayers().put(player.getName(), databasePresenter.getVotingDetails(player.getName()));
        }
    }
}
