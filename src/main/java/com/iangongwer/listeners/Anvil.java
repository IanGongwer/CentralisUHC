package com.iangongwer.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;

public class Anvil implements Listener {

    @EventHandler
    public void onAnvilItemPlacement(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Inventory inventory = event.getInventory();

            if (inventory instanceof AnvilInventory) {
                if (inventory.getItem(2) != null) {
                    if (inventory.getItem(2).getItemMeta().getDisplayName().equals("Golden Apple")) {
                        event.setCancelled(true);
                    }
                }
                if (inventory.getItem(0) != null && inventory.getItem(1) != null) {
                    if (inventory.getItem(0).getItemMeta().getDisplayName() != null) {
                        if (inventory.getItem(0).getItemMeta().getDisplayName().equals("Novice's Metal Sword")) {
                            event.setCancelled(true);
                        }
                    } else if (inventory.getItem(1).getItemMeta().getDisplayName() != null) {
                        if (inventory.getItem(1).getItemMeta().getDisplayName().equals("Novice's Metal Sword")) {
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }

}