package com.gmail.risterral.commands.notoped;

import com.gmail.risterral.achievements.AchievementDto;
import com.gmail.risterral.achievements.AchievementsPresenter;
import com.gmail.risterral.AchievementsPlugin;
import com.gmail.risterral.player.PlayersPresenter;
import com.gmail.risterral.utils.MultiLineInfo;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class AchievementCommand implements CommandExecutor {
    private static final String LIST_COMMANDS_LIST = "/achievement list [all] [page]\n/achievement list [have] [page]\n/achievement list [get] [page]\n";
    private static final String LIST_KEY = "list";
    private static final String ALL_KEY = "all";
    private static final String HAVE_KEY = "have";
    private static final String GET_KEY = "get";
    private static final String INFO_KEY = "info";
    private static final String INFO_ERROR = "There is no achievements named: ";
    private static final String POINTS_KEY = "points";
    private static final String NAME_KEY = "name";
    private static final String LEADERBOARD_KEY = "leaderboard";

    private Plugin plugin;

    public AchievementCommand(Plugin plugin) {
        this.plugin = plugin;
    }


    public boolean onCommand(CommandSender sender, Command command, String label, String[] split) {
        if (!(sender instanceof Player)) {
            return false;
        }
        Player player = (Player) sender;
        AchievementsPresenter achievementsPresenter = ((AchievementsPlugin) plugin).getAchievementsPresenter();
        PlayersPresenter playersPresenter = ((AchievementsPlugin) plugin).getPlayersPresenter();

        if (split.length > 0 && LIST_KEY.equals(split[0])) {
            int page = 1;
            if (split.length > 2) {
                try {
                    page = Integer.parseInt(split[2]);
                } catch (NumberFormatException e) {
                    return false;
                }
            }
            if (split.length == 2){
                if (ALL_KEY.equals(split[1])) {
                    StringBuilder allAchievementsSB = new StringBuilder();
                    for (AchievementDto achievementDto : achievementsPresenter.getAchievementsList()) {
                        if (achievementDto.isHidden()) {
                            continue;
                        }
                        allAchievementsSB.append(achievementDto.getName());
                        allAchievementsSB.append(" - ");
                        allAchievementsSB.append(achievementDto.getPoints());
                        allAchievementsSB.append(" points.\n");
                    }
                    MultiLineInfo.SendInfo(player, "Achievement list all", "achievement list all", allAchievementsSB.toString().split("\\n"), page);
                    return true;
                } else if (HAVE_KEY.equals(split[1])) {
                    StringBuilder allAchievementsSB = new StringBuilder();
                    for (AchievementDto achievementDto : playersPresenter.getPlayers().get(player.getName()).getAchievementsDone()) {
                        allAchievementsSB.append(achievementDto.getName());
                        allAchievementsSB.append(" - ");
                        allAchievementsSB.append(achievementDto.getPoints());
                        allAchievementsSB.append(" points.\n");
                    }
                    MultiLineInfo.SendInfo(player, "Achievement list have", "achievement list have", allAchievementsSB.toString().split("\\n"), page);
                    return true;
                } else if (GET_KEY.equals(split[1])) {
                    StringBuilder allAchievementsSB = new StringBuilder();
                    for (AchievementDto achievementDto : playersPresenter.getPlayers().get(player.getName()).getAchievementsUndone()) {
                        if (achievementDto.isHidden()) {
                            continue;
                        }
                        allAchievementsSB.append(achievementDto.getName());
                        allAchievementsSB.append(" - ");
                        allAchievementsSB.append(achievementDto.getPoints());
                        allAchievementsSB.append(" points.\n");
                    }
                    MultiLineInfo.SendInfo(player, "Achievement list get", "achievement list get", allAchievementsSB.toString().split("\\n"), page);
                    return true;
                } else {
                    return false;
                }
            } else {
                MultiLineInfo.SendInfo(player, "Achievement list", null, LIST_COMMANDS_LIST.toString().split("\\n"), 1);
                return true;
            }
        } else if (split.length > 1 && INFO_KEY.equals(split[0])) {
            StringBuilder nameSB = new StringBuilder();
            for (int i=1; i<split.length; i++) {
                nameSB.append(split[i]);
                nameSB.append(" ");
            }
            String name = nameSB.toString().substring(0, nameSB.length() - 1);
            for (AchievementDto achievementDto : playersPresenter.getPlayers().get(player.getName()).getAchievementsUndone()) {
                if (name.equals(achievementDto.getName()) && !achievementDto.isHidden()) {
                    MultiLineInfo.SendInfo(player, "Achievement info", null, getAchievemetnTemplate(achievementDto).toString().split("\\n"), 1);
                    return true;
                }
            }
            for (AchievementDto achievementDto : playersPresenter.getPlayers().get(player.getName()).getAchievementsDone()) {
                if (name.equals(achievementDto.getName()) && !achievementDto.isHidden()) {
                    MultiLineInfo.SendInfo(player, "Achievement info", null, getAchievemetnTemplate(achievementDto).toString().split("\\n"), 1);
                    return true;
                }
            }
            player.sendRawMessage(ChatColor.RED + (INFO_ERROR + name));
            return true;
        } else if (split.length > 0 && POINTS_KEY.equals(split[0])) {
            if (split.length == 1) {
                player.sendRawMessage("You have " + playersPresenter.getPlayers().get(player.getName()).getPoints() + " points.");
                return true;
            } else {
                player.sendRawMessage("TODO: ask Smack about database.");
                return true;
            }
        } else if (split.length == 1 && LEADERBOARD_KEY.equals(split[0])) {
            player.sendRawMessage("TODO: ask Smack about database.");
            return true;
        } else {
            return false;
        }
    }

    private String getAchievemetnTemplate(AchievementDto achievement) {
        StringBuilder achivementTemplate = new StringBuilder();
        achivementTemplate.append("Name: ");
        achivementTemplate.append(achievement.getName());
        achivementTemplate.append("\nType: ");
        achivementTemplate.append(achievement.getType());
        achivementTemplate.append("\nDescription: ");
        achivementTemplate.append(achievement.getDescription());
        achivementTemplate.append("\nPoints: ");
        achivementTemplate.append(achievement.getPoints());
        achivementTemplate.append(" points\nReward: ");
        achivementTemplate.append(achievement.getReward().toString());
        return achivementTemplate.toString();
    }
}
