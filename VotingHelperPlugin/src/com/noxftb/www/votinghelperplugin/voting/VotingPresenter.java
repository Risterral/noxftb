package com.noxftb.www.votinghelperplugin.voting;

import com.noxftb.www.votinghelperplugin.VotingHelperPlugin;
import com.noxftb.www.votinghelperplugin.database.DatabasePresenter;
import com.noxftb.www.votinghelperplugin.player.PlayerDto;
import com.noxftb.www.votinghelperplugin.player.PlayersPresenter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Date;


public class VotingPresenter {
    private static final Integer PERIOD = 2000;

    private final DatabasePresenter databasePresenter;
    private final PlayersPresenter playersPresenter;

    public VotingPresenter(JavaPlugin plugin) {
        this.databasePresenter = ((VotingHelperPlugin) plugin).getDatabasePresenter();
        this.playersPresenter = ((VotingHelperPlugin) plugin).getPlayersPresenter();

        new CheckForOutdatedVoting(plugin, this).runTaskTimer(plugin, PERIOD, PERIOD);
    }

    //TODO: add usage of this method.
    public void addVote(String playerName) {
        PlayerDto player = playersPresenter.getPlayers().get(playerName);
        player.setNumberOfVotes(player.getNumberOfVotes() + 1);
        player.setLastVotingDate(new Date());

        addPlayerToRegion(playerName);
        databasePresenter.updatePlayerVote(playerName, player.getNumberOfVotes(), player.getLastVotingDate());
    }

    public void addPlayerToRegion(String playerName) {
        //TODO: populate this method.
    }

    public void removePlayerFromRegion(String playerName) {
        //TODO: populate this method.
    }
}
