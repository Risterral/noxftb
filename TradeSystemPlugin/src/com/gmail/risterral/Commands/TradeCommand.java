package com.gmail.risterral.Commands;

import com.gmail.risterral.Player.PlayerDto;
import com.gmail.risterral.TradeSystemPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class TradeCommand implements CommandExecutor {

    private static String NO_TARGET_ERROR = "You have no target to trade with.";
    private static String TARGET_DONT_ACCEPT_TRADES = "Your target is not accepting trades.";
    private static String TARGET_IS_ALREADY_TRADING = "Your target is already trading with someone.";
    private static String TARGET_IS_BUSY = "Your target is busy.";
    private static int MAX_DISTANCE = 5;
    private static double X_DIMENSION_BUFFER = 0.8;
    private static double Y_DIMENSION_BUFFER_UP = 1.5;
    private static double Y_DIMENSION_BUFFER_DOWN = 0.5;
    private static double Z_DIMENSION_BUFFER = 0.8;

    private Plugin plugin;

    public TradeCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] split) {
        if (!(sender instanceof Player)) {
            return false;
        }
        Player playerSending = (Player) sender;
        Player target = playerTarget(playerSending, MAX_DISTANCE);
        if (target == null) {
            playerSending.sendRawMessage(ChatColor.RED + NO_TARGET_ERROR);
            return true;
        }
        PlayerDto targetDto = ((TradeSystemPlugin) plugin).getPlayersPresenter().getPlayers().get(target.getName());
        if (!targetDto.isAcceptingTrades() && !playerSending.isOp()) {
            playerSending.sendRawMessage(ChatColor.RED + TARGET_DONT_ACCEPT_TRADES);
            return true;
        }
        if (targetDto.isTrading()) {
            playerSending.sendRawMessage(ChatColor.RED + TARGET_IS_ALREADY_TRADING);
            return true;
        }
        if (targetDto.isBusy()) {
            playerSending.sendRawMessage(ChatColor.RED + TARGET_IS_BUSY);
            return true;
        }
        ((TradeSystemPlugin) plugin).getExchangesPresenter().start(playerSending, target);
        return true;
    }

    private Player playerTarget(Player player, int maxDistance) {
        List<Block> lineOfSight = null;

        for (Entity entity : player.getNearbyEntities(maxDistance, maxDistance, maxDistance)) {
            if (!(entity instanceof Player)) {
                continue;
            }
            Location targetLocation = entity.getLocation();
            if (lineOfSight == null) {
                lineOfSight = player.getLineOfSight(null, maxDistance);
            }
            for (Block block : lineOfSight) {
                if (Math.abs(targetLocation.getX() - block.getX()) > X_DIMENSION_BUFFER) {
                    continue;
                }
                if (Math.abs(targetLocation.getZ() - block.getZ()) > Z_DIMENSION_BUFFER) {
                    continue;
                }
                if ((targetLocation.getY() > block.getY()) && (targetLocation.getY() - block.getY() > Y_DIMENSION_BUFFER_DOWN)) {
                    continue;
                }
                if ((targetLocation.getY() < block.getY()) && (block.getY() - targetLocation.getY() > Y_DIMENSION_BUFFER_UP)) {
                    continue;
                }
                return (Player) entity;
//                if (targetLocation.getBlockX() == block.getX() && targetLocation.getBlockZ() == block.getZ()
//                        && (targetLocation.getBlockY() == block.getY() || targetLocation.getBlockY() + 1 == block.getY())) {
//                    return (Player) entity;
//                }
            }
        }
        return null;
    }
}
