package com.gmail.risterral.listeners;

import com.gmail.risterral.achievements.AchievementDto;
import com.gmail.risterral.achievements.AchievementsContainer;
import com.gmail.risterral.achievements.AchievementsPresenter;
import com.gmail.risterral.achievements.requirements.AbstractRequirement;
import com.gmail.risterral.achievements.requirements.DieRequirement;
import com.gmail.risterral.achievements.requirements.KillRequirement;
import com.gmail.risterral.achievements.requirements.RequirementType;
import com.gmail.risterral.AchievementsPlugin;
import com.gmail.risterral.database.DatabasePresenter;
import com.gmail.risterral.player.PlayerDto;
import com.gmail.risterral.utils.Position;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.Plugin;
import twilightforest.entity.EntityTFDeer;
import twilightforest.entity.EntityTFSquirrel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DeathListener implements Listener {
    private final Plugin plugin;
    private DatabasePresenter databasePresenter;
    private AchievementsPresenter achievementsPresenter;

    public DeathListener(Plugin instance) {
        plugin = instance;
        this.databasePresenter = ((AchievementsPlugin) plugin).getDatabasePresenter();
        this.achievementsPresenter = ((AchievementsPlugin) plugin).getAchievementsPresenter();
    }

    @EventHandler
    public void onPleayerDeath(PlayerDeathEvent event){
        Player player = event.getEntity();
        PlayerDto playerDto = ((AchievementsPlugin) plugin).getPlayersPresenter().getPlayers().get(player.getName());
        if (!playerDto.getEventsToCheck().contains(RequirementType.Die)) {
            return;
        }

        List<AchievementDto> achievementsToRemove = new ArrayList<AchievementDto>();

        for (long id : AchievementsPresenter.getAchievementsIdsByType(playerDto.getAchievementsUndone(), RequirementType.Die)) {
            for (AchievementDto achievementDto : playerDto.getAchievementsUndone()) {
                if (id == achievementDto.getId()) {
                    for (AbstractRequirement requirement : achievementDto.getRequirements()) {
                        if (requirement.getType().equals(RequirementType.Die)) {
                            HashMap<String, Object> status = new HashMap<String, Object>();
                            if (((KillRequirement) requirement).getWhere() != null) {
                                Location location = player.getLocation();
                                if (((DieRequirement) requirement).getWhere().getBiom() != null) {
                                    World world = player.getWorld();
                                    status.put(AchievementsPresenter.WHERE_KEY, new Position(world.getBiome(location.getBlockX(), location.getBlockZ()).name()));
                                } else {
                                    StringBuilder locationSB = new StringBuilder();
                                    locationSB.append(location.getBlockX());
                                    locationSB.append(" ");
                                    locationSB.append(location.getBlockY());
                                    locationSB.append(" ");
                                    locationSB.append(location.getBlockZ());
                                    status.put(AchievementsPresenter.WHERE_KEY, new Position(locationSB.toString()));
                                }
                            }
                            if (event.getEntity().getLastDamageCause() instanceof EntityDamageByEntityEvent) {
                                status.put(AchievementsPresenter.CAUSE_KEY, "Entity");

                                EntityDamageByEntityEvent nEvent = (EntityDamageByEntityEvent) event.getEntity().getLastDamageCause();
                                if ((nEvent.getDamager() instanceof Arrow)) {
                                    Arrow arrow = (Arrow) nEvent.getDamager();
                                    if (arrow.getShooter() instanceof Skeleton) {
                                        status.put(AchievementsPresenter.KILLER_KEY, "Skeleton");
                                    }
                                    if (arrow.getShooter() instanceof Player) {
                                        status.put(AchievementsPresenter.KILLER_KEY, "Player");
                                    }
                                } else {
                                    status.put(AchievementsPresenter.KILLER_KEY, nEvent.getDamager().toString().replaceAll("Craft", ""));
                                }
                            } else {
                                status.put(AchievementsPresenter.CAUSE_KEY, player.getLastDamageCause().getCause().toString());
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
            playerDto.getAchievementsUndone().remove(achievement);
            player.setMetadata("AchievementsUndone", new AchievementsContainer(plugin,playerDto.getAchievementsUndone()));
        }

    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event){
        Entity entity = event.getEntity();
        if(!(entity instanceof Player)){
            if(entity.getLastDamageCause() instanceof EntityDamageByEntityEvent){
                EntityDamageByEntityEvent entityDamageByEntityEvent = (EntityDamageByEntityEvent) entity.getLastDamageCause();
                if(entityDamageByEntityEvent.getDamager() instanceof Player){
                    Player killer = (Player)entityDamageByEntityEvent.getDamager();

                    killer.sendRawMessage("Entity name = " + entity.getType().name());
                    killer.sendRawMessage("Entity id = " + entity.getEntityId());
                    killer.sendRawMessage("Entity uniqe id = " + entity.getUniqueId());
                    killer.sendRawMessage("Entity type id = " + entity.getType().getTypeId());
                    killer.sendRawMessage("Entity type name = " + entity.getType().getName());
//                    killer.sendRawMessage("Entity type squirel = " + (entity instanceof EntityTFSquirrel));
//                    killer.sendRawMessage("Entity type dear = " + (entity instanceof EntityTFDeer));
                    PlayerDto playerDto = ((AchievementsPlugin) plugin).getPlayersPresenter().getPlayers().get(killer.getName());
                    if (!playerDto.getEventsToCheck().contains(RequirementType.Kill)) {
                        return;
                    }

                    List<AchievementDto> achievementsToRemove = new ArrayList<AchievementDto>();



                    for (long id : AchievementsPresenter.getAchievementsIdsByType(playerDto.getAchievementsUndone(), RequirementType.Kill)) {
                        for (AchievementDto achievementDto : playerDto.getAchievementsUndone()) {
                            if (id == achievementDto.getId()) {
                                for (AbstractRequirement requirement : achievementDto.getRequirements()) {
                                    if (requirement.getType().equals(RequirementType.Kill)) {
                                        HashMap<String, Object> status = new HashMap<String, Object>();

                                        status.put(AchievementsPresenter.WHAT_KEY, entity.getType().name().replaceAll("Craft", ""));


                                        if (((KillRequirement) requirement).getWhere() != null) {
                                            Location location = killer.getLocation();
                                            if (((KillRequirement) requirement).getWhere().getBiom() != null) {
                                                World world = killer.getWorld();
                                                status.put(AchievementsPresenter.WHERE_KEY, new Position(world.getBiome(location.getBlockX(), location.getBlockZ()).name()));
                                            } else {
                                                StringBuilder locationSB = new StringBuilder();
                                                locationSB.append(location.getBlockX());
                                                locationSB.append(" ");
                                                locationSB.append(location.getBlockY());
                                                locationSB.append(" ");
                                                locationSB.append(location.getBlockZ());
                                                status.put(AchievementsPresenter.WHERE_KEY, new Position(locationSB.toString()));
                                            }
                                        }
                                        status.put(AchievementsPresenter.WEAPON_KEY, killer.getItemInHand().getType().name());


                                        if (requirement.updateStatus(status)) {
                                            databasePresenter.updateAchievement(killer.getName(), id, achievementDto.toString());
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
                        playerDto.getAchievementsUndone().remove(achievement);
                        killer.setMetadata("AchievementsUndone", new AchievementsContainer(plugin,playerDto.getAchievementsUndone()));
                    }
                }
            }
        }
    }
}
