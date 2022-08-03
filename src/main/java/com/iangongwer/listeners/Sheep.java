package com.iangongwer.listeners;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import com.iangongwer.utils.Util;

public class Sheep implements Listener {

	Util u = Util.getInstance();

	@EventHandler
	public void onSheepKill(EntityDeathEvent event) {
		if (event.getEntityType().equals(EntityType.SHEEP)) {
			event.getDrops().add(new ItemStack(Material.STRING, 1));
		}
	}

}