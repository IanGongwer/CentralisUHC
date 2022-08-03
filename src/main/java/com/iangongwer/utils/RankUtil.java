package com.iangongwer.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.iangongwer.game.GameManager;

public class RankUtil {

    public static String getGroupPrefix(Player player) {
        if (isPlayerInGroup(player, "OWNER")) {
            return ChatColor.RED + "OWNER " + ChatColor.WHITE;
        }
        if (isPlayerInGroup(player, "STAFF")) {
            return ChatColor.BLUE + "STAFF " + ChatColor.WHITE;
        }
        if (isPlayerInGroup(player, "TWITCH")) {
            return ChatColor.LIGHT_PURPLE + "TWITCH " + ChatColor.WHITE;
        }
        if (isPlayerInGroup(player, "YOUTUBE")) {
            return ChatColor.YELLOW + "YOUTUBE " + ChatColor.WHITE;
        }
        if (GameManager.getInstance().isSpectator(player.getUniqueId())) {
            return ChatColor.GRAY + "SPEC " + ChatColor.WHITE;
        }
        return ChatColor.WHITE + "";

    }

    public static boolean isPlayerInGroup(Player player, String group) {
        return player.hasPermission("group." + group.toUpperCase());
    }

}