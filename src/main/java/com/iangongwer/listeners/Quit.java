package com.iangongwer.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import com.iangongwer.game.GameManager;
import com.iangongwer.game.GameState;
import com.iangongwer.team.TeamManager;
import com.iangongwer.utils.LobbyUtil;
import com.iangongwer.utils.Util;

public class Quit implements Listener {

	Util u = Util.getInstance();
	GameManager gm = GameManager.getInstance();
	TeamManager tm = TeamManager.getInstance();

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		event.setQuitMessage("");
		UUID playerUUID = event.getPlayer().getUniqueId();

		if (GameState.isLobby()) {
			deleteTeamIfLeaderQuits(playerUUID);
			gm.removePlayer(playerUUID);

			if (LobbyUtil.isPracticePlayer(playerUUID)) {
				LobbyUtil.removePracticePlayer(playerUUID);
			}
		}

		if (GameState.isScattering()) {
			gm.removePlayer(playerUUID);
			gm.removeScatteredPlayer(playerUUID);
			deleteTeamIfLeaderQuits(playerUUID);
		}

		if (GameState.isInGame()) {
			if (gm.isSpectator(playerUUID)) {
				gm.removeSpectator(playerUUID);
			} else if (gm.isPlayer(playerUUID)) {
				if (gm.isPvPEnabled()) {
					gm.getAlreadyScatteredPlayers().remove(playerUUID);
					gm.removePlayer(playerUUID);
					List<ItemStack> playerQuitInventory = new ArrayList<ItemStack>();
					for (ItemStack item : Bukkit.getPlayer(playerUUID).getInventory().getContents()) {
						playerQuitInventory.add(item);
					}
					gm.storeDeathInventories(playerUUID,
							playerQuitInventory);
					gm.getDeathLocations().put(playerUUID, Bukkit.getPlayer(playerUUID).getLocation());
					if (TeamManager.getInstance().areTeamsEnabled()) {
						TeamManager.getInstance().addDeceasedMember(playerUUID);
						TeamManager.getInstance().isFullTeamDead(playerUUID);
					}
					gm.isGameFinished();
				}
			}

		}

		if (GameState.isEnd()) {
			if (gm.isSpectator(playerUUID)) {
				gm.removeSpectator(playerUUID);
			} else if (u.isInStaffMode(playerUUID)) {
				u.removeStaffMode(playerUUID);
			} else if (gm.isPlayer(playerUUID)) {
				gm.removePlayer(playerUUID);
			}
		}
	}

	public void deleteTeamIfLeaderQuits(UUID playerUUID) {
		if (tm.hasTeam(playerUUID)) {
			if (tm.getTeamLeader(playerUUID) == playerUUID) {
				tm.deleteTeam(playerUUID);
			}
		}
	}

}