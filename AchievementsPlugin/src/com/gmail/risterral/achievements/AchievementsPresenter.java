package com.gmail.risterral.achievements;

import com.gmail.risterral.AchievementsPlugin;
import com.gmail.risterral.achievements.requirements.*;
import com.gmail.risterral.player.PlayerDto;
import com.gmail.risterral.utils.Fireworks;
import com.gmail.risterral.utils.ParsingUtil;
import com.gmail.risterral.utils.Position;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class AchievementsPresenter {

    public static final String ID_KEY = "Id";
    public static final String NAME_KEY = "Name";
    public static final String HIDDEN_KEY = "Hidden";
    public static final String TYPE_KEY = "Type";
    public static final String POINTS_KEY = "Points";
    public static final String DESCRIPTION_KEY = "Description";
    public static final String REQUIREMENTS_KEY = "Requirements";
    public static final String REWARD_KEY = "Reward";
    public static final String MESSAGE_KEY = "Message";
    public static final String EXPERIENCE_KEY = "Experience";
    public static final String ITEMS_KEY = "Items";
    public static final String LORE_KEY = "Lore";

    public static final String WHAT_KEY = "What";
    public static final String CAUSE_KEY = "Cause";
    public static final String AMOUNT_KEY = "Amount";
    public static final String WHERE_KEY = "Where";
    public static final String KILLER_KEY = "Killer";
    public static final String WEAPON_KEY = "Weapon";

    public static final String SPECIAL_KEY = "Special";

    private JavaPlugin plugin;
    private FileConfiguration config;
    private List<AchievementDto> achievementsList = new ArrayList<AchievementDto>();

    public AchievementsPresenter(JavaPlugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
        loadAchievementsConfig();
    }

    public AchievementDto getAchievementById(long id) {
        for (AchievementDto achievementDto : achievementsList) {
            if (achievementDto.getId() == id) {
                return achievementDto;
            }
        }
        return null;
    }

    public List<AchievementDto> getAchievementsList(List<Long> doneIds, boolean achievementsDone) {
        List<AchievementDto> result = new ArrayList<AchievementDto>();
        for (Long id : doneIds) {
            for (AchievementDto achievementDto : achievementsList) {
                if (id == achievementDto.getId() && achievementsDone) {
                    result.add(achievementDto);
                    break;
                }
            }
        }
        return result;
    }

    public static List<Long> getAchievementsIdsByType(List<AchievementDto> achievements, RequirementType type) {
        List<Long> result = new ArrayList<Long>();
        for (AchievementDto achievementDto : achievements) {
            for (AbstractRequirement requirement : achievementDto.getRequirements()) {
                if (type.equals(requirement.getType())) {
                    result.add(achievementDto.getId());
                    break;
                }
            }
        }
        return result;
    }

    public AchievementDto checkAchievementComplete(AchievementDto achievement, PlayerDto playerDto) {
        for (AbstractRequirement requirement : achievement.getRequirements()) {
            if (!requirement.isRequirementCompleted()) {
                return null;
            }
        }
        return grantAchievement(achievement, playerDto);
    }

    public AchievementDto grantAchievement(AchievementDto achievement, PlayerDto playerDto) {
        List<AchievementDto> achievementsDone = playerDto.getAchievementsDone();
        List<AchievementDto> achievementsUndone = playerDto.getAchievementsUndone();
        boolean isGranted = false;
        AchievementDto achievementToRemove = null;
        for (AchievementDto achievementDto : achievementsUndone) {
            if (achievementDto.getId() == achievement.getId()) {
                Player player = plugin.getServer().getPlayer(playerDto.getName());
                achievementsDone.add(achievement);
                playerDto.setPoints(playerDto.getPoints() + achievementDto.getPoints());
                ((AchievementsPlugin) plugin).getDatabasePresenter().updateAchievementStatus(player.getName(), achievementDto.getId(), 1);
                ((AchievementsPlugin) plugin).getDatabasePresenter().updatePlayerStatus(player.getName(), playerDto.getPoints(), playerDto.getObtainableRewards());
                informPlayer(player, achievement, false);
                Fireworks.shootFireworks(player);
                achievementToRemove = achievementDto;
                isGranted = true;
                break;
            }
        }
        if (isGranted) {
            return achievementToRemove;
        }
        return null;
    }

    private void informPlayer(Player player, AchievementDto achievementDto, boolean broadcast) {
        if (!broadcast) {
            player.sendRawMessage(ChatColor.GOLD + achievementDto.getReward().getMessage());
        } else {
            Bukkit.broadcastMessage(ChatColor.GOLD + (player.getDisplayName() + "Has just earned " + achievementDto.getName() + " achievement!"));
        }
        player.giveExp(achievementDto.getReward().getExperience());
        if (!achievementDto.getReward().getItems().isEmpty()) {
            if (achievementDto.getReward().getItems().size() == 1) {
                player.sendRawMessage(ChatColor.GREEN + "You have 1 more reward item to obtain.");
            } else {
                player.sendRawMessage(ChatColor.GREEN + "You have " + achievementDto.getReward().getItems().size() + " more reward items to obtain.");
            }
        }
    }

    private void loadAchievementsConfig() {

        config.options().copyDefaults(true);
        plugin.saveConfig();

        int achievementIterator = 0;
        for (String key : config.getKeys(false)) {
            plugin.getLogger().info("Loading achievement number: " + achievementIterator++);
            achievementsList.add(parseAchievement(config.getConfigurationSection(key)));
        }
    }

    private AchievementDto parseAchievement(ConfigurationSection configurationSection) {
        AchievementDto achievement = new AchievementDto();
        achievement.setId(configurationSection.getLong(ID_KEY));
        achievement.setName(configurationSection.getString(NAME_KEY));
        achievement.setHidden(configurationSection.getBoolean(HIDDEN_KEY));
        achievement.setType(configurationSection.getString(TYPE_KEY));
        achievement.setPoints(configurationSection.getInt(POINTS_KEY));
        achievement.setDescription(configurationSection.getString(DESCRIPTION_KEY));
        achievement.setRequirements(parseRequirements(configurationSection.getConfigurationSection(REQUIREMENTS_KEY), achievement.getId()));
        achievement.setReward(parseReward(configurationSection.getConfigurationSection(REWARD_KEY)));

        plugin.getLogger().info("Loading achievement " + achievement.getName() + " succeeded.");

        return achievement;
    }

    private List<AbstractRequirement> parseRequirements(ConfigurationSection configurationSection, long achievementId) {
        List<AbstractRequirement> requirements = new ArrayList<AbstractRequirement>();
        String value;
        for (String key : configurationSection.getKeys(false)) {
            if ("Place".equals(key)) {
                PlaceRequirement requirement = new PlaceRequirement();
                requirement.setType(RequirementType.Place);
                requirement.setAchievementId(achievementId);
                requirement.setWhatItem(configurationSection.getString(key + "." + WHAT_KEY));
                requirement.setAmount(configurationSection.getInt(key + "." + AMOUNT_KEY));
                value = configurationSection.getString(key + "." + WHERE_KEY);
                if (value != null) {
                    requirement.setWhere(new Position(value));
                }
                requirements.add(requirement);
            } else if ("Die".equals(key)) {
                DieRequirement requirement = new DieRequirement();
                requirement.setType(RequirementType.Die);
                requirement.setAchievementId(achievementId);
                requirement.setCause(configurationSection.getString(key + "." + CAUSE_KEY));
                requirement.setAmount(configurationSection.getInt(key + "." + AMOUNT_KEY));
                value = configurationSection.getString(key + "." + WHERE_KEY);
                if (value != null) {
                    requirement.setWhere(new Position(value));
                }
                value = configurationSection.getString(key + "." + KILLER_KEY);
                if (value != null) {
                    requirement.setKiller(value);
                }
                requirements.add(requirement);
            } else if ("Kill".equals(key)) {
                KillRequirement requirement = new KillRequirement();
                requirement.setType(RequirementType.Kill);
                requirement.setAchievementId(achievementId);
                requirement.setWhat(configurationSection.getString(key + "." + WHAT_KEY));
                requirement.setAmount(configurationSection.getInt(key + "." + AMOUNT_KEY));
                value = configurationSection.getString(key + "." + WHERE_KEY);
                if (value != null) {
                    requirement.setWhere(new Position(value));
                }
                value = configurationSection.getString(key + "." + WEAPON_KEY);
                if (value != null) {
                    requirement.setWeapon(value);
                }
                requirements.add(requirement);
            } else if ("Special".equals(key)) {
                SpecialRequirement requirement = new SpecialRequirement();
                requirement.setType(RequirementType.Special);
                requirement.setAchievementId(achievementId);
                requirement.setDescription(configurationSection.getString(SPECIAL_KEY));
                requirements.add(requirement);
            } else {
                plugin.getLogger().info("Unknown requirement while loading achievementId = " + achievementId);
            }
        }

        return requirements;
    }

    private RewardDto parseReward(ConfigurationSection configurationSection) {
        RewardDto rewardDto = new RewardDto();
        rewardDto.setMessage(configurationSection.getString(MESSAGE_KEY));
        rewardDto.setExperience(configurationSection.getInt(EXPERIENCE_KEY));
        ConfigurationSection value = configurationSection.getConfigurationSection(ITEMS_KEY);
        if (value != null) {
            for (String key : value.getKeys(false)) {
                rewardDto.getItems().add(parseRewardItem(configurationSection.getConfigurationSection(ITEMS_KEY + "." + key)));
            }

        }

        return rewardDto;
    }

    private RewardItem parseRewardItem(ConfigurationSection configurationSection) {
        RewardItem rewardItem = new RewardItem();
        rewardItem.setId(configurationSection.getInt(ID_KEY));
        rewardItem.setAmount(configurationSection.getInt(AMOUNT_KEY));
        String value = configurationSection.getString(DESCRIPTION_KEY);
        if (value != null) {
            rewardItem.setDescription(value);
        }
        value = configurationSection.getString(LORE_KEY);
        if (value != null) {
            rewardItem.setLore(value);
        }

        return rewardItem;
    }

    public AchievementDto parseAchievementFromDatabase(String achievement) {
        AchievementDto achievementDto = new AchievementDto();
        Integer startIndex;

        startIndex = achievement.indexOf("id=") + 3;
        achievementDto.setId(Long.parseLong(achievement.substring(startIndex, achievement.indexOf(",", startIndex))));

        startIndex = achievement.indexOf("name='", startIndex) + 6;
        achievementDto.setName(achievement.substring(startIndex, achievement.indexOf("',", startIndex)));

        startIndex = achievement.indexOf("hidden=", startIndex) + 7;
        achievementDto.setHidden("true".equals(achievement.substring(startIndex, achievement.indexOf(",", startIndex))));

        startIndex = achievement.indexOf("type='", startIndex) + 6;
        achievementDto.setType(achievement.substring(startIndex, achievement.indexOf("',", startIndex)));

        startIndex = achievement.indexOf("points=", startIndex) + 7;
        achievementDto.setPoints(Integer.parseInt(achievement.substring(startIndex, achievement.indexOf(",", startIndex))));

        startIndex = achievement.indexOf("description='", startIndex) + 13;
        achievementDto.setDescription(achievement.substring(startIndex, achievement.indexOf("',", startIndex)));

        startIndex = achievement.indexOf("requirements=[", startIndex) + 14;
        String requirementsString = achievement.substring(startIndex, achievement.indexOf("],", startIndex));
        List<AbstractRequirement> requirements = new ArrayList<AbstractRequirement>();
        for (String requirementString : requirementsString.split("}, \\w+Requirement\\{")) {
            requirements.add(parseRequirementFromDatabase(requirementString));
        }
        achievementDto.setRequirements(requirements);

        startIndex = achievement.indexOf("reward=", startIndex) + 7;
        achievementDto.setReward(new RewardDto(achievement.substring(startIndex, achievement.indexOf("}", startIndex) + 1)));

        return achievementDto;
    }

    private AbstractRequirement parseRequirementFromDatabase(String requirement) {
        Integer index = requirement.indexOf("type=") + 5;
        String type = requirement.substring(index, requirement.indexOf(",", index));
        if ("Die".equals(type)) {
            return new DieRequirement(requirement);
        } else if ("Kill".equals(type)) {
            return new KillRequirement(requirement);
        } else if ("Place".equals(type)) {
            return new PlaceRequirement(requirement);
        } else if ("Special".equals(type)) {
            return new SpecialRequirement(requirement);
        }

        return null;
    }

    public List<AchievementDto> getAchievementsList() {
        return achievementsList;
    }

    public void setAchievementsList(List<AchievementDto> achievementsList) {
        this.achievementsList = achievementsList;
    }

}
