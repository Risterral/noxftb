package com.gmail.risterral.Player;

import java.util.HashMap;

public class PlayersPresenter {
    private HashMap<String, PlayerDto> players;

    public PlayersPresenter() {
        this.players = new HashMap<String, PlayerDto>();
    }

    public HashMap<String, PlayerDto> getPlayers() {
        return players;
    }
}
