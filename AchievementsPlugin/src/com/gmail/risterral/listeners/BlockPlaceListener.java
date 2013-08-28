package com.gmail.risterral.listeners;

import com.gmail.risterral.achievements.AchievementDto;
import com.gmail.risterral.achievements.AchievementsContainer;
import com.gmail.risterral.achievements.AchievementsPresenter;
import com.gmail.risterral.achievements.requirements.AbstractRequirement;
import com.gmail.risterral.achievements.requirements.PlaceRequirement;
import com.gmail.risterral.achievements.requirements.RequirementType;
import com.gmail.risterral.AchievementsPlugin;
import com.gmail.risterral.database.DatabasePresenter;
import com.gmail.risterral.player.PlayerDto;
import com.gmail.risterral.utils.Position;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BlockPlaceListener implements Listener {
    private Plugin plugin;
    private DatabasePresenter databasePresenter;
    private AchievementsPresenter achievementsPresenter;

    public BlockPlaceListener(Plugin plugin) {
        this.plugin = plugin;
        this.databasePresenter = ((AchievementsPlugin) plugin).getDatabasePresenter();
        this.achievementsPresenter = ((AchievementsPlugin) plugin).getAchievementsPresenter();
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block blockPlaced = event.getBlockPlaced();
        Player player = event.getPlayer();


        player.sendRawMessage("Entity type name = " + blockPlaced.getType().name());
        player.sendRawMessage("Entity typeid = " + blockPlaced.getTypeId());
        player.sendRawMessage("Entity type id = " + blockPlaced.getType().getId());
        player.sendRawMessage("Entity type class = " + blockPlaced.getType().getClass());
        player.sendRawMessage("Biom  name = " + blockPlaced.getBiome().name());


        PlayerDto playerDto = ((AchievementsPlugin) plugin).getPlayersPresenter().getPlayers().get(player.getName());
        if (!playerDto.getEventsToCheck().contains(RequirementType.Place)) {
            return;
        }

        List<AchievementDto> achievementsToRemove = new ArrayList<AchievementDto>();

        for (long id : AchievementsPresenter.getAchievementsIdsByType(playerDto.getAchievementsUndone(), RequirementType.Place)) {
            for (AchievementDto achievementDto : playerDto.getAchievementsUndone()) {
                if (id == achievementDto.getId()) {
                    for (AbstractRequirement requirement : achievementDto.getRequirements()) {
                        if (requirement.getType().equals(RequirementType.Place)) {
                            HashMap<String, Object> status = new HashMap<String, Object>();
                            status.put(AchievementsPresenter.WHAT_KEY, blockPlaced.getType().name());
                            if (((PlaceRequirement) requirement).getWhere() != null) {
                                if (((PlaceRequirement) requirement).getWhere().getBiom() != null) {
                                    status.put(AchievementsPresenter.WHERE_KEY, new Position(blockPlaced.getBiome().name()));
                                } else {
                                    StringBuilder locationSB = new StringBuilder();
                                    locationSB.append(player.getLocation().getBlockX());
                                    locationSB.append(" ");
                                    locationSB.append(player.getLocation().getBlockY());
                                    locationSB.append(" ");
                                    locationSB.append(player.getLocation().getBlockZ());
                                    status.put(AchievementsPresenter.WHERE_KEY, new Position(locationSB.toString()));
                                }
                            }
                            if (requirement.updateStatus(status)) {
                                databasePresenter.updateAchievement(player.getName(), id, achievementDto.toString());
//                                player.setMetadata("AchievementsUndone", new AchievementsContainer(plugin, playerDto.getAchievementsUndone()));
                                AchievementDto checkResult = achievementsPresenter.checkAchievementComplete(achievementDto, playerDto);
                                if (checkResult != null) {
                                    achievementsToRemove.add(checkResult);
                                }
                            }
                        }
                    }
                    break;
                }
            }
        }

        for (AchievementDto achievement : achievementsToRemove) {
//            player.removeMetadata("AchievementsUndone", plugin);
            playerDto.getAchievementsUndone().remove(achievement);
            player.setMetadata("AchievementsUndone", new AchievementsContainer(plugin, playerDto.getAchievementsUndone()));
        }


    }
}
