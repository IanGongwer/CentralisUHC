package me.centy.uhc.listeners;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import me.centy.uhc.game.GameManager;
import me.centy.uhc.game.Util;

public class Break implements Listener {

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		if (Util.getInstance().isLobby()) {
			event.setCancelled(true);
		}
		if (Util.getInstance().isScattering()) {
			event.setCancelled(true);
		}
		if (Util.getInstance().isInGame()) {
			if (GameManager.getInstance().isSpectator(player.getUniqueId())
					|| Util.getInstance().isInStaffMode(player.getUniqueId())) {
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
		}
		if (Util.getInstance().isEnd()) {
			event.setCancelled(true);
		}
		if (player.isOp()) {
			event.setCancelled(false);
		}
	}

}