package com.iangongwer.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

import com.iangongwer.game.GameManager;

public class DisableGodAppleCraft implements Listener {

	GameManager gm = GameManager.getInstance();

	@EventHandler
	public void onToolCraft(PrepareItemCraftEvent event) {
		ItemStack item = event.getRecipe().getResult();
		CraftingInventory inventory = event.getInventory();
		if (item.getType().equals(Material.GOLDEN_APPLE) && item.getDurability() == 1) {
			inventory.setResult(new ItemStack(Material.AIR));
		}
	}

}