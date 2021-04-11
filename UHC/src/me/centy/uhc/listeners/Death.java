package me.centy.uhc.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import me.centy.uhc.game.GameManager;
import me.centy.uhc.game.Util;

public class Death implements Listener {

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		Player killer = event.getEntity().getKiller();
		if (Util.getInstance().isLobby()) {
			event.setDeathMessage(ChatColor.GREEN + killer.getDisplayName() + ChatColor.WHITE + " has killed "
					+ ChatColor.GREEN + player.getDisplayName());
		}
		if (!Util.getInstance().isLobby()) {
			if (!GameManager.getInstance().isPvPEnabled()) {
				player.sendMessage(Util.getInstance().messageFormat("Use /latescatter for another chance.", "a"));
			}
			if (killer instanceof Player) {
				event.setDeathMessage(ChatColor.GREEN + killer.getDisplayName() + ChatColor.WHITE + " has killed "
						+ ChatColor.GREEN + player.getDisplayName());
				GameManager.getInstance().setPlayerKills(killer.getUniqueId(),
						GameManager.getInstance().getPlayerKills(killer.getUniqueId()) + 1);
				Util.getInstance().updateKills(killer);
			} else {
				event.setDeathMessage(ChatColor.GREEN + player.getDisplayName() + ChatColor.WHITE + " has been killed");
			}
			// List<ItemStack> drops = event.getDrops();
			// for (ItemStack item : drops) {
			// event.getEntity().getWorld().dropItemNaturally(player.getLocation(), item);
			// }
			// ItemStack goldenApple = new ItemStack(Material.GOLDEN_APPLE);
			// ItemMeta gapMeta = goldenApple.getItemMeta();
			// gapMeta.setDisplayName(player.getDisplayName() + "'s Head");
			// goldenApple.setItemMeta(gapMeta);
			// event.getEntity().getWorld().dropItemNaturally(player.getLocation(),
			// goldenApple);
			event.setKeepInventory(false);
			GameManager.getInstance().removePlayer(player.getUniqueId());
			GameManager.getInstance().addSpectator(player.getUniqueId());
			Util.getInstance().spawnFireworks(player.getLocation(), 2);
			GameManager.getInstance().isGameFinished();
		}
	}

}