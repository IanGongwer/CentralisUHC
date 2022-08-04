package com.iangongwer.crafts;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public class StringCraft {

    public static void createStringCraft() {
        ShapedRecipe shapedrecipe = new ShapedRecipe(new ItemStack(Material.STRING));
        shapedrecipe.shape("WW ", "W  ");
        shapedrecipe.setIngredient('W', Material.WOOL);
        Bukkit.getServer().addRecipe(shapedrecipe);
    }

}