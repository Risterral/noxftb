package com.gmail.risterral.Commands;

import com.gmail.risterral.Player.PlayerDto;
import com.gmail.risterral.TradeSystemPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class RejectTradesCommand implements CommandExecutor {

    private static String CONFIRM_INFO = "You are now rejecting trades.";

    private Plugin plugin;

    public RejectTradesCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] split) {
        if (!(sender instanceof Player)) {
            return false;
        }
        if ((split.length != 1) && split[0].equals("trades")) {
            return false;
        }
        Player player = (Player) sender;
        PlayerDto playerDto = ((TradeSystemPlugin) plugin).getPlayersPresenter().getPlayers().get(player.getName());
        playerDto.setAcceptingTrades(false);
        player.sendRawMessage(ChatColor.WHITE + CONFIRM_INFO);

        return true;
    }
}
