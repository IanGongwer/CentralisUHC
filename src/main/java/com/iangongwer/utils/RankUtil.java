package com.iangongwer.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.iangongwer.game.GameManager;
import com.iangongwer.mysql.ConnectionMYSQL;

public class RankUtil {

    public static String getGroupPrefix(Player player) {
        if (isPlayerInGroup(player, "OWNER")) {
            return ChatColor.GOLD + "[Rank " + ConnectionMYSQL.getInstance().getPlayerRank(player.getUniqueId())
                    + "] " + ChatColor.RED + "[OWNER] " + ChatColor.WHITE;
        }
        if (isPlayerInGroup(player, "STAFF")) {
            return ChatColor.GOLD + "[Rank " + ConnectionMYSQL.getInstance().getPlayerRank(player.getUniqueId())
                    + "] " + ChatColor.BLUE + "[STAFF] " + ChatColor.WHITE;
        }
        if (isPlayerInGroup(player, "TWITCH")) {
            return ChatColor.GOLD + "[Rank " + ConnectionMYSQL.getInstance().getPlayerRank(player.getUniqueId())
                    + "] " + ChatColor.LIGHT_PURPLE + "[TWITCH] " + ChatColor.WHITE;
        }
        if (isPlayerInGroup(player, "YOUTUBE")) {
            return ChatColor.GOLD + "[Rank " + ConnectionMYSQL.getInstance().getPlayerRank(player.getUniqueId())
                    + "] " + ChatColor.YELLOW + "[YOUTUBE] " + ChatColor.WHITE;
        }
        if (GameManager.getInstance().isSpectator(player.getUniqueId())) {
            return ChatColor.GOLD + "[Rank " + ConnectionMYSQL.getInstance().getPlayerRank(player.getUniqueId())
                    + "] " + ChatColor.GRAY + "[SPEC] " + ChatColor.WHITE;
        }
        return ChatColor.GOLD + "[Rank " + ConnectionMYSQL.getInstance().getPlayerRank(player.getUniqueId())
                + "] " + ChatColor.WHITE;

    }

    public static boolean isPlayerInGroup(Player player, String group) {
        return player.hasPermission("group." + group.toUpperCase());
    }

}