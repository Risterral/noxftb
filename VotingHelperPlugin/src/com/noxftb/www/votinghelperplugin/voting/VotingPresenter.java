package com.noxftb.www.votinghelperplugin.voting;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class VotingPresenter {
    private final JavaPlugin plugin;

    public VotingPresenter(JavaPlugin plugin) {
        this.plugin = plugin;


        new CheckForOutdatedVoting(plugin).runTaskLater(plugin, 1);
    }

    //TODO: update player.
//    public void addVote(String playerName)
}
