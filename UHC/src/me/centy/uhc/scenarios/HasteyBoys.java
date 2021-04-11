package me.centy.uhc.scenarios;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.centy.uhc.game.GameManager;

public class HasteyBoys implements Listener {

	@EventHandler
	public void onToolCraft(PrepareItemCraftEvent event) {
		if (GameManager.getInstance().isScenarioActive("HasteyBoys")) {

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
		ItemMeta itemmeta = item.getItemMeta();
		itemmeta.addEnchant(Enchantment.DIG_SPEED, 3, true);
		itemmeta.addEnchant(Enchantment.DURABILITY, 3, true);
		item.setItemMeta(itemmeta);
	}

}