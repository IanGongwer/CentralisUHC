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

        ShapedRecipe shapedrecipe2 = new ShapedRecipe(new ItemStack(Material.STRING));
        shapedrecipe2.shape(" WW", " W ");
        shapedrecipe2.setIngredient('W', Material.WOOL);
        Bukkit.getServer().addRecipe(shapedrecipe2);

        ShapedRecipe shapedrecipe3 = new ShapedRecipe(new ItemStack(Material.STRING));
        shapedrecipe3.shape("   ", "WW ", "W  ");
        shapedrecipe3.setIngredient('W', Material.WOOL);
        Bukkit.getServer().addRecipe(shapedrecipe3);

        ShapedRecipe shapedrecipe4 = new ShapedRecipe(new ItemStack(Material.STRING));
        shapedrecipe4.shape("   ", " WW", " W ");
        shapedrecipe4.setIngredient('W', Material.WOOL);
        Bukkit.getServer().addRecipe(shapedrecipe4);
    }

}