package com.iangongwer.utils;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.iangongwer.main.Main;
import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.menu.SGMenu;

public class InventoryUtil {

    public static void createSpectatorInventory(Player spectator, Player player) {
        SGMenu spectatorInventory = Main.spiGUI.create("&a" + player.getDisplayName() + "'s Inventory", 3);

        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null) {
                spectatorInventory.addButton(new SGButton(item));
            }
        }

        for (ItemStack item : player.getInventory().getArmorContents()) {
            if (item != null) {
                spectatorInventory.addButton(new SGButton(item));
            }
        }

        spectator.openInventory(spectatorInventory.getInventory());
    }

}