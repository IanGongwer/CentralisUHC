package com.iangongwer.listeners;

import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import com.iangongwer.game.GameManager;
import com.iangongwer.game.GameState;
import com.iangongwer.utils.Util;

public class Break implements Listener {

	Util u = Util.getInstance();
	GameManager gm = GameManager.getInstance();

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		UUID playerUUID = player.getUniqueId();

		if (GameState.isInGame()) {
			if (gm.isSpectator(playerUUID) || u.isInStaffMode(playerUUID)) {
				event.setCancelled(true);
			} else {
				if (event.getBlock().getType().equals(Material.STONE)
						&& (event.getBlock().getData() == 1 || event.getBlock().getData() == 2
								|| event.getBlock().getData() == 3 || event.getBlock().getData() == 4
								|| event.getBlock().getData() == 5 || event.getBlock().getData() == 6)) {
					event.setCancelled(true);
					event.getBlock().setType(Material.AIR);
					player.getWorld().dropItem(event.getBlock().getLocation(), new ItemStack(Material.COBBLESTONE));
				}
				if (event.getBlock().getType().equals(Material.LEAVES)
						|| event.getBlock().getType().equals(Material.LEAVES_2)) {
					Random random = new Random();
					int rate = random.nextInt(101);
					if (rate <= 4) {
						Location appleLocation = event.getBlock().getLocation();
						Bukkit.getWorld("uhc_world").dropItemNaturally(appleLocation, new ItemStack(Material.APPLE));
					}
				}
				if (event.getBlock().getType().equals(Material.GRAVEL)) {
					Random random = new Random();
					int rate = random.nextInt(101);
					if (rate <= 4) {
						Location flintLocation = event.getBlock().getLocation();
						Bukkit.getWorld("uhc_world").dropItemNaturally(flintLocation, new ItemStack(Material.FLINT));
					}
				}
				event.setCancelled(false);
			}
		} else {
			event.setCancelled(true);
		}
	}

}