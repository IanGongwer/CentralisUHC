package com.iangongwer.listeners;

import java.text.DecimalFormat;

import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.iangongwer.game.GameManager;
import com.iangongwer.game.GameState;
import com.iangongwer.utils.LobbyUtil;
import com.iangongwer.utils.Util;

public class PvP implements Listener {

	Util u = Util.getInstance();
	GameManager gm = GameManager.getInstance();

	@EventHandler
	public void onPlayerPvP(EntityDamageByEntityEvent event) {
		if (event.getEntity().getType() == EntityType.PLAYER && event.getDamager().getType() == EntityType.PLAYER) {
			Player player = (Player) event.getEntity();
			Player damager = (Player) event.getDamager();
			if (GameState.isLobby()) {
				if (LobbyUtil.isPracticePlayer(player.getUniqueId())
						&& LobbyUtil.isPracticePlayer(damager.getUniqueId())) {
					event.setCancelled(false);
				}
				if (!LobbyUtil.isPracticePlayer(player.getUniqueId())
						&& !LobbyUtil.isPracticePlayer(damager.getUniqueId())) {
					event.setCancelled(true);
				}
				if (!LobbyUtil.isPracticePlayer(player.getUniqueId())
						&& LobbyUtil.isPracticePlayer(damager.getUniqueId())) {
					event.setCancelled(true);
				}

				if (LobbyUtil.isPracticePlayer(player.getUniqueId())
						&& !LobbyUtil.isPracticePlayer(damager.getUniqueId())) {
					event.setCancelled(true);
				}
			}
			if (GameState.isScattering()) {
				event.setCancelled(true);
			}
			if (GameState.isInGame()) {
				if (gm.getSpectators().contains(damager.getUniqueId())
						|| gm.getSpectators().contains(player.getUniqueId())) {
					event.setCancelled(true);
				}
				if (!gm.getSpectators().contains(damager.getUniqueId())
						&& !gm.getSpectators().contains(player.getUniqueId())) {
					if (gm.isPvPEnabled()) {
						event.setCancelled(false);
					} else {
						event.setCancelled(true);
					}
				}
			}
			if (GameState.isEnd()) {
				event.setCancelled(true);
			}
		}
		if (GameState.isInGame()) {
			if (event.getEntity() instanceof Player) {
				if (event.getDamager().getType() == EntityType.ARROW) {
					Arrow arrow = (Arrow) event.getDamager();
					if (arrow.getShooter() instanceof Player) {
						if (gm.isPvPEnabled()) {
							event.setCancelled(false);
						} else {
							event.setCancelled(true);
						}
					}
				}
			}
			if (event.getEntity() instanceof Player) {
				if (event.getDamager().getType() == EntityType.FISHING_HOOK) {
					if (gm.isPvPEnabled()) {
						event.setCancelled(false);
					} else {
						event.setCancelled(true);
					}
				}
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
					if (GameState.isLobby()) {
						if (!LobbyUtil.isPracticePlayer(player.getUniqueId())) {
							event.setCancelled(true);
						} else {
							shooter.sendMessage(ChatColor.GREEN + player.getDisplayName() + ChatColor.WHITE + " is at "
									+ ChatColor.GREEN + df.format(playerHealth) + ChatColor.WHITE + " hearts");
						}
					} else if (GameState.isInGame()) {
						shooter.sendMessage(ChatColor.GREEN + player.getDisplayName() + ChatColor.WHITE + " is at "
								+ ChatColor.GREEN + df.format(playerHealth) + ChatColor.WHITE + " hearts");
					}
				}
			}
		}
	}

}