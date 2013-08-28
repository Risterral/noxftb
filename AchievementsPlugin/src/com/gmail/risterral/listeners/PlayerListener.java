package com.gmail.risterral.listeners;

import com.gmail.risterral.achievements.*;
import com.gmail.risterral.achievements.requirements.AbstractRequirement;
import com.gmail.risterral.achievements.requirements.RequirementType;
import com.gmail.risterral.database.DatabasePresenter;
import com.gmail.risterral.player.PlayerDto;
import com.gmail.risterral.player.PlayersPresenter;
import com.gmail.risterral.AchievementsPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class PlayerListener implements Listener {
    private final Plugin plugin;

    public PlayerListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayersPresenter playersPresenter = ((AchievementsPlugin) plugin).getPlayersPresenter();
        AchievementsPresenter achievementsPresenter = ((AchievementsPlugin) plugin).getAchievementsPresenter();
        DatabasePresenter databasePresenter = ((AchievementsPlugin) plugin).getDatabasePresenter();
        PlayerDto playerDto;

        databasePresenter.getPlayerAchievementsDone(player.getName());

        if (!playersPresenter.getPlayers().containsKey(player.getName())) {
            playerDto = new PlayerDto();

            List<AchievementDto> achievementsDone = databasePresenter.getPlayerAchievementsDone(player.getName());
            List<AchievementDto> achievementsUndone = databasePresenter.getPlayerAchievementsUndone(player.getName());

            if (achievementsUndone.size() == 0) {
                achievementsUndone = achievementsPresenter.getAchievementsList();
                for (AchievementDto achievementDto : achievementsUndone) {
                    databasePresenter.insertAchievement(player.getName(), achievementDto.toString());
                }

                playerDto.setPoints(0);
                playerDto.setObtainableRewards(new ArrayList<RewardItem>());


            } else {

                playerDto.setPoints(0);
                playerDto.setObtainableRewards(new ArrayList<RewardItem>());

            }

            playerDto.setName(player.getName());
            playerDto.setAchievementsDone(achievementsDone);
            playerDto.setAchievementsUndone(achievementsUndone);
            playerDto.setEventsToCheck(getEventsToCheckList(achievementsUndone));

//            if(player.hasMetadata("AchievementsUndone")) {
//                achievementsUndone = (List<AchievementDto>) player.getMetadata("AchievementsUndone").get(0).value();
//                List<Long> ids = new ArrayList<Long>();
//                int size = achievementsUndone.size();
//                for (AchievementDto achievementDto : achievementsUndone) {
//                    ids.add(achievementDto.getId());
//                }
//                if(player.hasMetadata("AchievementsDone")) {
//                    achievementsDone = (List<AchievementDto>) player.getMetadata("AchievementsDone").get(0).value();
//                    for (AchievementDto achievementDto : achievementsDone) {
//                        ids.add(achievementDto.getId());
//                    }
//                }
//                for (AchievementDto achievementDto : achievementsPresenter.getAchievementsList()) {
//                    if (!ids.contains(achievementDto.getId())) {
//                        achievementsUndone.add(achievementDto);
//                    }
//                }
//            } else {
//                achievementsUndone = achievementsPresenter.getAchievementsList();
//                player.setMetadata("AchievementsUndone", new AchievementsContainer(plugin, achievementsUndone));
//            }


            playersPresenter.getPlayers().put(player.getName(), playerDto);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        plugin.getLogger().info(event.getPlayer().getName() + " left the server! :'(");
    }

    private List<RequirementType> getEventsToCheckList(List<AchievementDto> achievementsUndone) {
        List<RequirementType> eventsToCheck = new ArrayList<RequirementType>();
        for (AchievementDto achievementDto : achievementsUndone) {
            for (AbstractRequirement requirement : achievementDto.getRequirements()) {
                if(!eventsToCheck.contains(requirement.getType())) {
                    eventsToCheck.add(requirement.getType());
                }
            }
        }
        return eventsToCheck;
    }
}
