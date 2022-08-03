package com.iangongwer.utils;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.iangongwer.team.TeamManager;

public class ChatUtil {

    private static boolean chatMuted = false;
    private static ArrayList<UUID> mutedPlayers = new ArrayList<UUID>();

    public static ArrayList<UUID> getMutedPlayers() {
        return mutedPlayers;
    }

    public static void addMutedPlayer(UUID playerUUID) {
        mutedPlayers.add(playerUUID);
    }

    public static void removeMutedPlayer(UUID playerUUID) {
        mutedPlayers.remove(playerUUID);
    }

    public static boolean isMuted(UUID playerUUID) {
        return mutedPlayers.contains(playerUUID);
    }

    public static String chatMessage(String message, Player player) {
        if (TeamManager.getInstance().getTeam(player.getUniqueId()) != null) {
            return RankUtil.getGroupPrefix(player) + ChatColor.GOLD + "["
                    + Bukkit.getPlayer(TeamManager.getInstance().getTeamLeader(player.getUniqueId())).getDisplayName()
                            .toUpperCase()
                    + "] " + ChatColor.WHITE + player.getDisplayName() + ": " + message;
        }
        return RankUtil.getGroupPrefix(player) + player.getDisplayName() + ": " + message;
    }

    public static boolean isChatMuted() {
        return chatMuted;
    }

    public static void setChatMute(boolean mute) {
        chatMuted = mute;
    }

}