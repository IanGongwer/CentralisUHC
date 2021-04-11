package me.centy.uhc.listeners;

import java.text.DecimalFormat;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import me.centy.uhc.game.GameManager;
import me.centy.uhc.game.Util;
import net.md_5.bungee.api.ChatColor;

public class PvP implements Listener {

	@EventHandler
	public void onPlayerPvP(EntityDamageByEntityEvent event) {
		if (event.getEntity().getType() == EntityType.PLAYER && event.getDamager().getType() == EntityType.PLAYER) {
			Player player = (Player) event.getEntity();
			Player damager = (Player) event.getDamager();
			if (Util.getInstance().isLobby()) {
				if (Util.getInstance().isPracticePlayer(player.getUniqueId())
						&& Util.getInstance().isPracticePlayer(damager.getUniqueId())) {
					event.setCancelled(false);
				}
				if (!Util.getInstance().isPracticePlayer(player.getUniqueId())
						&& !Util.getInstance().isPracticePlayer(damager.getUniqueId())) {
					event.setCancelled(true);
				}
				if (!Util.getInstance().isPracticePlayer(player.getUniqueId())
						&& Util.getInstance().isPracticePlayer(damager.getUniqueId())) {
					event.setCancelled(true);
				}

				if (Util.getInstance().isPracticePlayer(player.getUniqueId())
						&& !Util.getInstance().isPracticePlayer(damager.getUniqueId())) {
					event.setCancelled(true);
				}
			}
			if (Util.getInstance().isScattering()) {
				event.setCancelled(true);
			}
			if (Util.getInstance().isInGame()) {
				if (GameManager.getInstance().getSpectators().contains(damager.getUniqueId())
						|| GameManager.getInstance().getSpectators().contains(player.getUniqueId())) {
					event.setCancelled(true);
				}
				if (!GameManager.getInstance().getSpectators().contains(damager.getUniqueId())
						&& !GameManager.getInstance().getSpectators().contains(player.getUniqueId())) {
					if (GameManager.getInstance().isPvPEnabled()) {
						event.setCancelled(false);
						/*
						 * GameManager.combatLogTime.put(player.getUniqueId(), 60);
						 * GameManager.combatLogTime.put(damager.getUniqueId(), 60);
						 */
					} else {
						event.setCancelled(true);
					}
				}
			}
			if (Util.getInstance().isEnd()) {
				event.setCancelled(true);
			}
		}
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if (event.getDamager().getType() == EntityType.ARROW) {
				Arrow arrow = (Arrow) event.getDamager();
				if (arrow.getShooter() instanceof Player) {
					Player shooter = (Player) arrow.getShooter();
					double playerHealth = player.getHealth() / 2;
					DecimalFormat df = new DecimalFormat("#.#");
					if (Util.getInstance().isLobby()) {
						if (!Util.getInstance().isPracticePlayer(player.getUniqueId())) {
							event.setCancelled(true);
						} else {
							shooter.sendMessage(ChatColor.GREEN + player.getDisplayName() + ChatColor.WHITE + " is at "
									+ ChatColor.GREEN + df.format(playerHealth) + ChatColor.WHITE + " hearts");
						}
					}
					if (Util.getInstance().isInGame()) {
						shooter.sendMessage(ChatColor.GREEN + player.getDisplayName() + ChatColor.WHITE + " is at "
								+ ChatColor.GREEN + df.format(playerHealth) + ChatColor.WHITE + " hearts");
					}
				}
			}
		}
	}

}