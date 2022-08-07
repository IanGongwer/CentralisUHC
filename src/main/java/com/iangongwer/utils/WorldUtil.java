package com.iangongwer.utils;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.meta.FireworkMeta;

import com.iangongwer.game.GameManager;

public class WorldUtil {

    static GameManager gm = GameManager.getInstance();

    public static void spawnVillager(Player player) {
        Villager quitLoggedVillager = (Villager) player.getWorld().spawnEntity(player.getLocation(),
                EntityType.VILLAGER);
        quitLoggedVillager.setCustomName(player.getDisplayName());
        quitLoggedVillager.setCustomNameVisible(true);
    }

    public static void despawnVillager(OfflinePlayer player) {
        for (LivingEntity entity : Bukkit.getWorld("uhc_world").getLivingEntities()) {
            if (entity instanceof Villager && entity.getCustomName() != null) {
                if (entity.getCustomName()
                        .equalsIgnoreCase(player.getName())) {
                    entity.damage(20);
                    gm.setPvPLogTime(player.getUniqueId(), -1);
                    gm.removePvPLoggedPlayer(player.getUniqueId());
                }
            }
        }
    }

    public static void createWorld(String worldName) {
        if (Bukkit.getWorld(worldName) == null) {
            File deleteFilePath = new File(Bukkit.getWorldContainer() + "/" + worldName);
            deleteWorld(deleteFilePath);
            WorldCreator worldcreate = new WorldCreator(worldName);
            worldcreate.environment(World.Environment.NORMAL);
            worldcreate.type(WorldType.NORMAL);
            worldcreate.createWorld();
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "wb " + worldName + " set 1000 1000 0 0");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "wb " + worldName + " fill");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "wb fill confirm");
            clearZeroZero();
        }
    }

    public static void clearZeroZero() {
        for (int x = -100; x <= 100; x++) {
            for (int y = 40; y <= 100; y++) {
                for (int z = -100; z <= 100; z++) {
                    Block block = Bukkit.getServer().getWorld("uhc_world").getBlockAt(x, y, z);
                    if (block.getType() == Material.LOG || block.getType() == Material.LOG_2
                            || block.getType() == Material.LEAVES || block.getType() == Material.LEAVES_2
                            || block.getType() == Material.HUGE_MUSHROOM_1
                            || block.getType() == Material.HUGE_MUSHROOM_2) {
                        block.setType(Material.AIR);
                    }
                }
            }
        }
    }

    public static boolean deleteWorld(File path) {
        if (path.exists()) {
            File files[] = path.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteWorld(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        return (path.delete());
    }

    public static void spawnFireworks(Location location, int amount) {
        Location loc = location;
        Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();

        fwm.setPower(2);
        fwm.addEffect(FireworkEffect.builder().withColor(Color.RED).flicker(true).build());

        fw.setFireworkMeta(fwm);
        fw.detonate();

        for (int i = 0; i < amount; i++) {
            Firework fw2 = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
            fw2.setFireworkMeta(fwm);
        }
    }

}