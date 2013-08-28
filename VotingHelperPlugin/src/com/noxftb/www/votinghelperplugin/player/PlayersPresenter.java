package com.noxftb.www.votinghelperplugin.player;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class PlayersPresenter {

    private JavaPlugin plugin;
    private HashMap<String, PlayerDto> players;

    public PlayersPresenter(JavaPlugin plugin) {
        this.plugin = plugin;
        this.players = new HashMap<String, PlayerDto>();
    }

    public HashMap<String, PlayerDto> getPlayers() {
        return players;
    }

    public void setPlayers(HashMap<String, PlayerDto> players) {
        this.players = players;
    }
}
