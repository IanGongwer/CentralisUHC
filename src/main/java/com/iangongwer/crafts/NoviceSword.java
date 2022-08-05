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

public class NoviceSword {

    public static void createNoviceSword() {
        ItemStack item = new ItemStack(Material.IRON_SWORD, 1);
        ItemMeta itemmeta = item.getItemMeta();
        itemmeta.setDisplayName("Novice's Metal Sword");
        List<String> itemlore = new ArrayList<>();
        itemlore.add("This powerful sword is an artifact");
        itemlore.add("from the great Dispersed war!");
        itemmeta.setLore(itemlore);
        itemmeta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
        itemmeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(itemmeta);

        ShapedRecipe shapedrecipe = new ShapedRecipe(item);
        shapedrecipe.shape(" R ", " I ", " S ");
        shapedrecipe.setIngredient('R', Material.REDSTONE);
        shapedrecipe.setIngredient('I', Material.IRON_SWORD);
        shapedrecipe.setIngredient('S', Material.STICK);
        Bukkit.getServer().addRecipe(shapedrecipe);

        ShapedRecipe shapedrecipe2 = new ShapedRecipe(item);
        shapedrecipe2.shape("R  ", "I  ", "S  ");
        shapedrecipe2.setIngredient('R', Material.REDSTONE);
        shapedrecipe2.setIngredient('I', Material.IRON_SWORD);
        shapedrecipe2.setIngredient('S', Material.STICK);
        Bukkit.getServer().addRecipe(shapedrecipe2);

        ShapedRecipe shapedrecipe3 = new ShapedRecipe(item);
        shapedrecipe3.shape("  R", "  I", "  S");
        shapedrecipe3.setIngredient('R', Material.REDSTONE);
        shapedrecipe3.setIngredient('I', Material.IRON_SWORD);
        shapedrecipe3.setIngredient('S', Material.STICK);
        Bukkit.getServer().addRecipe(shapedrecipe3);
    }

}