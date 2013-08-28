package com.noxftb.www.votinghelperplugin.voting;

import com.noxftb.www.votinghelperplugin.VotingHelperPlugin;
import com.noxftb.www.votinghelperplugin.database.DatabasePresenter;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Level;

public class CheckForOutdatedVoting extends BukkitRunnable {

    private static final String CHECK_START_MESSAGE = "Checking for players with outdated voting.";
    private static final String CHECK_END_MESSAGE = "Checking ended.";
    private static final Integer OUTDATED_TIME_IN_HOURS = 6;

    private final JavaPlugin plugin;
    private final DatabasePresenter databasePresenter;
    private final VotingPresenter votingPresenter;

    public CheckForOutdatedVoting(JavaPlugin plugin, VotingPresenter votingPresenter) {
        this.plugin = plugin;
        this.databasePresenter = ((VotingHelperPlugin) plugin).getDatabasePresenter();
        this.votingPresenter = votingPresenter;
    }

    @Override
    public void run() {
        this.plugin.getLogger().log(Level.INFO, CHECK_START_MESSAGE);
        for (String playerName : databasePresenter.getOutdatedPlayers(OUTDATED_TIME_IN_HOURS)) {
            votingPresenter.removePlayerFromRegion(playerName);
        }
        this.plugin.getLogger().log(Level.INFO, CHECK_END_MESSAGE);
    }
}
