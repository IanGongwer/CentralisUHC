package com.iangongwer.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.iangongwer.game.GameManager;
import com.iangongwer.runnables.GameRunnable;
import com.iangongwer.runnables.ScatterRunnable;

import fr.minuskube.netherboard.Netherboard;
import fr.minuskube.netherboard.bukkit.BPlayerBoard;

public class ScoreboardUtil {

    public static void createLobbyScoreboard(Player player) {
        BPlayerBoard board = Netherboard.instance().createBoard(player, "uhc.centralis.cc");
        board.set("Waiting for players...", 15);
    }

    public static void createScatterScoreboard(Player player) {
        if (hasScoreboard(player)) {
            BPlayerBoard board = getScoreboard(player);
            board.set("Look in Chat for", 15);
            board.set("Game Information", 14);
        } else {
            BPlayerBoard board = Netherboard.instance().createBoard(player, "uhc.centralis.cc");
            board.set(ChatColor.BOLD + "Time", 15);
            board.set(ScatterRunnable.getFormattedTime(), 14);
        }
    }

    public static void createGameScoreboard(Player player) {
        if (hasScoreboard(player)) {
            BPlayerBoard board = getScoreboard(player);
            board.set(ChatColor.BOLD + "Time", 15);
            board.set(GameRunnable.getFormattedTime(), 14);
            board.set(ChatColor.BOLD + "Kills", 13);
            int playerKills = GameManager.getInstance().getPlayerKills(player.getUniqueId());
            board.set(String.valueOf(playerKills), 12);
        } else {
            BPlayerBoard board = Netherboard.instance().createBoard(player, "uhc.centralis.cc");
            board.set(ChatColor.BOLD + "Time", 15);
            board.set(GameRunnable.getFormattedTime(), 14);
            board.set(ChatColor.BOLD + "Kills", 13);
            int playerKills = GameManager.getInstance().getPlayerKills(player.getUniqueId());
            board.set(String.valueOf(playerKills), 12);
        }
    }

    public static void createStaffSpecScoreboard(Player player) {
        if (hasScoreboard(player)) {
            BPlayerBoard board = getScoreboard(player);
            board.set(ChatColor.BOLD + "Time", 15);
            board.set(GameRunnable.getFormattedTime(), 14);
        } else {
            BPlayerBoard board = Netherboard.instance().createBoard(player, "uhc.centralis.cc");
            board.set(ChatColor.BOLD + "Time", 15);
            board.set(GameRunnable.getFormattedTime(), 14);
        }
    }

    public static boolean hasScoreboard(Player player) {
        BPlayerBoard board = Netherboard.instance().getBoard(player);
        if (board == null) {
            return false;
        } else {
            return true;
        }
    }

    public static BPlayerBoard getScoreboard(Player player) {
        BPlayerBoard board = Netherboard.instance().getBoard(player);
        return board;
    }

    public static void updateGameTime(Player player) {
        BPlayerBoard board = getScoreboard(player);
        board.set(GameRunnable.getFormattedTime(), 14);
    }

    public static void updateKills(Player player) {
        BPlayerBoard board = getScoreboard(player);
        int playerKills = GameManager.getInstance().getPlayerKills(player.getUniqueId());
        board.set(String.valueOf(playerKills), 12);
    }

}