package com.iangongwer.crafts;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

public class LegendSword {

    public static void createLegendSword() {
        ItemStack item = new ItemStack(Material.DIAMOND_SWORD, 1);
        ItemMeta itemmeta = item.getItemMeta();
        itemmeta.setDisplayName("Legend's Crystalized Sword");
        List<String> itemlore = new ArrayList<>();
        itemlore.add("This noble sword is the King's");
        itemlore.add("from the great Dispersed war!");
        itemmeta.setLore(itemlore);
        itemmeta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
        itemmeta.addEnchant(Enchantment.DURABILITY, 1, true);
        itemmeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(itemmeta);

        ShapedRecipe shapedrecipe = new ShapedRecipe(item);
        shapedrecipe.shape(" D ", " I ", "OOO");
        shapedrecipe.setIngredient('O', Material.OBSIDIAN);
        shapedrecipe.setIngredient('D', Material.DIAMOND);
        shapedrecipe.setIngredient('I', Material.IRON_SWORD);
        Bukkit.getServer().addRecipe(shapedrecipe);
    }

}