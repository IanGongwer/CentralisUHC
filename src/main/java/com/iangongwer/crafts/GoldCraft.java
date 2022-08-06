package com.iangongwer.crafts;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public class GoldCraft {

    public static void createGoldCraft() {
        ShapedRecipe shapedrecipe = new ShapedRecipe(new ItemStack(Material.GOLD_INGOT, 4));
        shapedrecipe.shape("II ");
        shapedrecipe.setIngredient('I', Material.IRON_BLOCK);
        Bukkit.getServer().addRecipe(shapedrecipe);
    }

}