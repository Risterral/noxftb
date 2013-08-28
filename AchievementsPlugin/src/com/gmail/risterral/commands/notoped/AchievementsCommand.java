package com.gmail.risterral.commands.notoped;

import com.gmail.risterral.utils.MultiLineInfo;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AchievementsCommand implements CommandExecutor {
    private static final String COMMANDS_LIST = "/achievements [page]\n/achievements whatis\n/achievement list\n/achievement list [all] [page]\n/achievement list [have] [page]\n/achievement list [get] [page]\n/achievement info [name]\n/achievement points\n/achievement points [name]\n/achievement leaderboard\n/rewards\n/reward claim [name]\n";
    private static final String ABOUT_DESCRIPTION = "Here will be nice description, TODO: ask \"Grama Nazi\" about it.";
    private static final String WHAT_IT_IS_KEY = "whatis";

    public boolean onCommand(CommandSender sender, Command command, String label, String[] split) {
        if (!(sender instanceof Player)) {
            return false;
        }
        Player player = (Player) sender;

        if (split.length == 0) {
            MultiLineInfo.SendInfo(player, "Achievements", "achievements", COMMANDS_LIST.split("\\n"), 1);
            return true;
        } else if (split.length == 1) {
            if (WHAT_IT_IS_KEY.equals(split[0])) {
                MultiLineInfo.SendInfo(player, "Achievements whatis", null, ABOUT_DESCRIPTION.split("\\n"), 1);
                return true;
            }
            try {
                int page = Integer.parseInt(split[0]);
                MultiLineInfo.SendInfo(player, "Achievements", "achievements", COMMANDS_LIST.split("\\n"), page);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        } else {
            return false;
        }
    }
}
