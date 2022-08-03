package com.iangongwer.scenarios;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

public class SupplyDrops {

    static Random random = new Random();

    public static void spawnSupplyDrop(int x, int y, int z) {
        Block supplyDrop = Bukkit.getWorld("uhc_world").getBlockAt(x, y, z);
        supplyDrop.setType(Material.CHEST);

        Chest chest = (Chest) supplyDrop.getState();
        ArrayList<Material> possibleItems = new ArrayList<Material>(
                Arrays.asList(Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS,
                        Material.DIAMOND_BOOTS, Material.DIAMOND_SWORD, Material.GOLDEN_APPLE, Material.DIAMOND,
                        Material.GOLD_INGOT, Material.ARROW, Material.STRING, Material.ENCHANTMENT_TABLE));

        for (int i = 0; i < 4; i++) {
            int item = random.nextInt(11);
            if (possibleItems.get(item) == Material.ARROW) {
                chest.getInventory().addItem(new ItemStack(possibleItems.get(item), 32));
            }
            if (possibleItems.get(item) == Material.GOLDEN_APPLE) {
                chest.getInventory().addItem(new ItemStack(possibleItems.get(item), 3));
            }
            if (possibleItems.get(item) == Material.DIAMOND) {
                chest.getInventory().addItem(new ItemStack(possibleItems.get(item), 2));
            }
            if (possibleItems.get(item) == Material.STRING) {
                chest.getInventory().addItem(new ItemStack(possibleItems.get(item), 3));
            }
            if (possibleItems.get(item) == Material.GOLD_INGOT) {
                chest.getInventory().addItem(new ItemStack(possibleItems.get(item), 9));
            }
            if (possibleItems.get(item) != Material.ARROW && possibleItems.get(item) != Material.GOLDEN_APPLE
                    && possibleItems.get(item) != Material.DIAMOND && possibleItems.get(item) != Material.STRING
                    && possibleItems.get(item) != Material.GOLD_INGOT) {
                chest.getInventory().addItem(new ItemStack(possibleItems.get(item), 1));
            }
        }
    }

}