package com.iangongwer.scenarios;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.iangongwer.game.GameManager;

public class HasteyBoys implements Listener {

	GameManager gm = GameManager.getInstance();

	@EventHandler
	public void onToolCraft(PrepareItemCraftEvent event) {
		if (gm.isScenarioActive("HasteyBoys")) {
			ItemStack item = event.getRecipe().getResult();
			CraftingInventory inventory = event.getInventory();
			String name = item.getType().name();

			if ((name.contains("SPADE")) || (name.contains("AXE")) || (name.contains("PICKAXE"))) {
				addCustomEnchantments(item);
				inventory.setResult(item);
			}

		}
	}

	public void addCustomEnchantments(ItemStack item) {
		String itemname = item.getType().name();
		if (itemname.contains("SPADE")) {
			ItemMeta itemmeta = item.getItemMeta();
			itemmeta.setDisplayName("Quick-Witted Spade");
			List<String> itemlore = new ArrayList<>();
			itemlore.add("This delicate spade was crafted");
			itemlore.add("at the hilltop of the blacksmith");
			itemmeta.setLore(itemlore);
			itemmeta.addEnchant(Enchantment.DIG_SPEED, 3, true);
			itemmeta.addEnchant(Enchantment.DURABILITY, 3, true);
			itemmeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			item.setItemMeta(itemmeta);
		}
		if (itemname.contains("AXE")) {
			ItemMeta itemmeta = item.getItemMeta();
			itemmeta.setDisplayName("Quick-Witted Axe");
			List<String> itemlore = new ArrayList<>();
			itemlore.add("This wonderful axe was hand-carved");
			itemlore.add("from the Blossom tree");
			itemmeta.setLore(itemlore);
			itemmeta.addEnchant(Enchantment.DIG_SPEED, 3, true);
			itemmeta.addEnchant(Enchantment.DURABILITY, 3, true);
			itemmeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			item.setItemMeta(itemmeta);
		}
		if (itemname.contains("PICKAXE")) {
			ItemMeta itemmeta = item.getItemMeta();
			itemmeta.setDisplayName("Quick-Witted Pickaxe");
			List<String> itemlore = new ArrayList<>();
			itemlore.add("This sharp pickaxe was designed");
			itemlore.add("to mine through the hardest materials");
			itemmeta.setLore(itemlore);
			itemmeta.addEnchant(Enchantment.DIG_SPEED, 3, true);
			itemmeta.addEnchant(Enchantment.DURABILITY, 3, true);
			itemmeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			item.setItemMeta(itemmeta);
		}
	}

}