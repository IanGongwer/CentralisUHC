package com.iangongwer.listeners;

import java.text.DecimalFormat;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.iangongwer.game.GameManager;
import com.iangongwer.game.GameState;
import com.iangongwer.runnables.PvPLogRunnable;
import com.iangongwer.team.TeamManager;
import com.iangongwer.utils.HeartUtil;
import com.iangongwer.utils.LobbyUtil;
import com.iangongwer.utils.ScoreboardUtil;
import com.iangongwer.utils.Util;

public class PvP implements Listener {

	Util u = Util.getInstance();
	GameManager gm = GameManager.getInstance();
	PvPLogRunnable pr = new PvPLogRunnable();

	@EventHandler
	public void onPlayerPvP(EntityDamageByEntityEvent event) {
		if (event.getEntity().getType() == EntityType.PLAYER && event.getDamager().getType() == EntityType.PLAYER) {
			Player player = (Player) event.getEntity();
			UUID playerUUID = player.getUniqueId();
			Player damager = (Player) event.getDamager();
			UUID damagerUUID = damager.getUniqueId();

			if (player != null) {
				HeartUtil.showHealth(player, ScoreboardUtil.getScoreboard(player).getScoreboard(),
						ScoreboardUtil.getScoreboard(player).getName());
			}

			if (GameState.isLobby()) {
				if (LobbyUtil.isPracticePlayer(playerUUID)
						&& LobbyUtil.isPracticePlayer(damagerUUID)) {
					event.setCancelled(false);
				} else {
					event.setCancelled(true);
				}
			}

			if (GameState.isScattering() || GameState.isEnd()) {
				event.setCancelled(true);
			}
			if (GameState.isInGame()) {
				if (gm.getSpectators().contains(damagerUUID)
						|| gm.getSpectators().contains(playerUUID) || u.isInStaffMode(damagerUUID)
						|| u.isInStaffMode(playerUUID)) {
					event.setCancelled(true);
				}

				if (TeamManager.getInstance().areTeamsEnabled()) {
					if (TeamManager.getInstance().getTeamMembers(damagerUUID).contains(playerUUID)) {
						event.setCancelled(true);
					}
				}
				if (!TeamManager.getInstance().areTeamsEnabled()) {
					if (!gm.getSpectators().contains(damagerUUID)
							&& !gm.getSpectators().contains(playerUUID) && !u.isInStaffMode(damagerUUID)
							&& !u.isInStaffMode(playerUUID)) {
						if (gm.isPvPEnabled()) {
							event.setCancelled(false);
							if (!gm.isPvPLogged(playerUUID)) {
								player.sendMessage(u.messageFormat("You are now PvP logged for 5 seconds", "c"));
							}
							if (!gm.isPvPLogged(damagerUUID)) {
								damager.sendMessage(u.messageFormat("You are now PvP logged for 5 seconds", "c"));
							}
							gm.setPvPLogTime(playerUUID, 5);
							gm.setPvPLogTime(damagerUUID, 5);
						} else {
							event.setCancelled(true);
						}
					}
				} else {
					if (!gm.getSpectators().contains(damagerUUID)
							&& !gm.getSpectators().contains(playerUUID) && !u.isInStaffMode(damagerUUID)
							&& !u.isInStaffMode(playerUUID)
							&& !TeamManager.getInstance().getTeamMembers(damagerUUID).contains(playerUUID)) {
						if (gm.isPvPEnabled()) {
							event.setCancelled(false);
							if (!gm.isPvPLogged(playerUUID)) {
								player.sendMessage(u.messageFormat("You are now PvP logged for 5 seconds", "c"));
							}
							if (!gm.isPvPLogged(damagerUUID)) {
								damager.sendMessage(u.messageFormat("You are now PvP logged for 5 seconds", "c"));
							}
							gm.setPvPLogTime(playerUUID, 5);
							gm.setPvPLogTime(damagerUUID, 5);
						} else {
							event.setCancelled(true);
						}
					}
				}
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
						if (gm.isPvPEnabled()) {
							shooter.sendMessage(ChatColor.GREEN + player.getDisplayName() + ChatColor.WHITE + " is at "
									+ ChatColor.GREEN + df.format(playerHealth) + ChatColor.WHITE + " hearts");
						}
					}
				}
			}
		}
	}

}