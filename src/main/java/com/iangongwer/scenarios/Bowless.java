package com.iangongwer.scenarios;

import org.bukkit.Material;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

import com.iangongwer.game.GameManager;

public class Bowless implements Listener {

	GameManager gm = GameManager.getInstance();

	@EventHandler
	public void onBowCraft(CraftItemEvent event) {
		if (gm.isScenarioActive("Bowless")) {
			ItemStack item = event.getRecipe().getResult();
			ItemStack bow = new ItemStack(Material.BOW);

			if (item.equals(bow)) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onSkeletonDeath(EntityDeathEvent event) {
		if (gm.isScenarioActive("Bowless")) {
			if (event.getEntity() instanceof Skeleton) {
				event.getDrops().clear();
				event.getDrops().add(new ItemStack(Material.BONE, 1));
				event.getDrops().add(new ItemStack(Material.ARROW, 1));
			}
		}
	}

}