package com.iangongwer.listeners;

import java.util.Random;

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
		if (GameState.isInGame()) {
			if (gm.isSpectator(player.getUniqueId()) || u.isInStaffMode(player.getUniqueId())) {
				event.setCancelled(true);
			} else {
				if (event.getBlock().getType().equals(Material.LEAVES)
						|| event.getBlock().getType().equals(Material.LEAVES_2)) {
					Random random = new Random();
					int rate = random.nextInt(101);
					if (rate <= 5) {
						Location appleLocation = event.getBlock().getLocation();
						Bukkit.getWorld("uhc_world").dropItemNaturally(appleLocation, new ItemStack(Material.APPLE));
					}
				}
				if (event.getBlock().getType().equals(Material.GRAVEL)) {
					Random random = new Random();
					int rate = random.nextInt(101);
					if (rate <= 5) {
						Location flintLocation = event.getBlock().getLocation();
						Bukkit.getWorld("uhc_world").dropItemNaturally(flintLocation, new ItemStack(Material.FLINT));
					}
				}
				event.setCancelled(false);
			}
		} else {
			event.setCancelled(true);
		}
		if (player.isOp()) {
			event.setCancelled(false);
		}
	}

}