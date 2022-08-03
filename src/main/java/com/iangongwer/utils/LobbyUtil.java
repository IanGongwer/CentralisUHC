package com.iangongwer.utils;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.iangongwer.game.GameManager;

public class LobbyUtil {

    static GameManager gm = GameManager.getInstance();
    static Util u = Util.getInstance();

    private static Location lobbySpawnPoint = new Location(Bukkit.getWorld("world"), 1.5, 19.0, 2.5, -90.0f, 0.0f);
    private static Location practiceSpawnPoint = new Location(Bukkit.getWorld("world"), 464.5, 107.0, 512.5, -180.0f,
            0.0f);
    private static ArrayList<UUID> practicePlayers = new ArrayList<UUID>();

    public static ArrayList<UUID> getPracticePlayers() {
        return practicePlayers;
    }

    public static void addPracticePlayer(UUID playerUUID) {
        practicePlayers.add(playerUUID);
        practiceInventory(Bukkit.getPlayer(playerUUID));
    }

    public static void removePracticePlayer(UUID playerUUID) {
        practicePlayers.remove(playerUUID);

        Player player = Bukkit.getPlayer(playerUUID);
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.teleport(getLobbySpawnPoint());
        player.sendMessage(u.messageFormat("You have left practice mode. Use /prac to join practice mode.", "c"));
    }

    public static boolean isPracticePlayer(UUID playerUUID) {
        if (practicePlayers.contains(playerUUID)) {
            return true;
        } else {
            return false;
        }
    }

    public static void practiceInventory(Player player) {
        player.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET));
        player.getInventory().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
        player.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
        player.getInventory().setBoots(new ItemStack(Material.DIAMOND_BOOTS));
        player.getInventory().addItem(new ItemStack(Material.IRON_SWORD));
        player.getInventory().addItem(new ItemStack(Material.BOW));
        player.getInventory().addItem(new ItemStack(Material.ARROW, 64));
        player.getInventory().addItem(new ItemStack(Material.FISHING_ROD));
        player.teleport(getPracticeSpawnPoint());
        player.sendMessage(u.messageFormat("You are now in practice mode. Use /prac to leave practice mode.", "a"));
    }

    public static void joinLobbyUtil(Player player) {
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.setFoodLevel(20);
        player.setMaxHealth(20);
        player.setHealth(20);
        player.sendMessage("");
        player.sendMessage("Welcome to Centralis UHC. Use /prac to join the practice arena.");
        player.sendMessage("");
        player.teleport(getLobbySpawnPoint());
        player.getActivePotionEffects().clear();
        player.setExp(0);
        player.setGameMode(GameMode.SURVIVAL);
        gm.addPlayer(player.getUniqueId());
        ScoreboardUtil.createLobbyScoreboard(player);
    }

    public static Location getPracticeSpawnPoint() {
        return practiceSpawnPoint;
    }

    public static void setPracticeSpawnPoint(String world, double x, double y, double z, float pitch, float yaw) {
        practiceSpawnPoint = new Location(Bukkit.getWorld(world), x, y, z, pitch, yaw);
    }

    public static Location getLobbySpawnPoint() {
        return lobbySpawnPoint;
    }

    public static void setLobbySpawnPoint(String world, double x, double y, double z, float pitch, float yaw) {
        lobbySpawnPoint = new Location(Bukkit.getWorld(world), x, y, z, pitch, yaw);
    }

}