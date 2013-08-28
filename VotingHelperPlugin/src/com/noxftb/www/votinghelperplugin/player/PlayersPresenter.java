package com.noxftb.www.votinghelperplugin.player;

import com.noxftb.www.votinghelperplugin.VotingHelperPlugin;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class PlayersPresenter {

    private HashMap<String, PlayerDto> players;

    public PlayersPresenter(JavaPlugin plugin) {
        this.players = new HashMap<String, PlayerDto>();

        for (OfflinePlayer offlinePlayer : plugin.getServer().getOfflinePlayers()) {
            this.players.put(offlinePlayer.getName(), ((VotingHelperPlugin) plugin).getDatabasePresenter().getVotingDetails(offlinePlayer.getName()));
        }
    }

    public HashMap<String, PlayerDto> getPlayers() {
        return players;
    }
}
