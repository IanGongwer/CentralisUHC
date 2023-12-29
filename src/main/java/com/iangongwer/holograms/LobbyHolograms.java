package com.iangongwer.holograms;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import com.iangongwer.main.Main;
import com.iangongwer.mysql.ConnectionMYSQL;
import com.iangongwer.redis.ConnectionRedis;
import com.iangongwer.utils.Util;

public class LobbyHolograms {

	Main main = Main.getInstance();
	ConnectionMYSQL dbm = ConnectionMYSQL.getInstance();
	ConnectionRedis cr = ConnectionRedis.getInstance();
	Util u = Util.getInstance();

	Location loc = new Location(Bukkit.getWorld("world"), -240, 47, -640);
	Location killsLoc = new Location(Bukkit.getWorld("world"), -240, 46.65, -640);
	Location deathsLoc = new Location(Bukkit.getWorld("world"), -240, 46.3, -640);
	Location winsLoc = new Location(Bukkit.getWorld("world"), -240, 45.95, -640);

	public void createLobbyHologram() {
		for (Entity entity : Bukkit.getWorld("world").getEntities()) {
			if (entity.getType().equals(EntityType.ARMOR_STAND)) {
				entity.remove();
			}
		}

		// ArmorStand hologram = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
		// hologram.setVisible(false);
		// hologram.setGravity(false);
		// hologram.setCustomNameVisible(true);
		// hologram.setCustomName("Server Statistics");
		// mostKills(loc);
		// mostDeaths(loc);
		// mostWins(loc);
	}

	@Deprecated
	public void mostKills(Location loc) {
		ArmorStand hologram = (ArmorStand) loc.getWorld().spawnEntity(killsLoc, EntityType.ARMOR_STAND);
		hologram.setVisible(false);
		hologram.setGravity(false);
		hologram.setCustomNameVisible(true);

		if (!Main.isRedisEnabled()) {
			UUID mostPlayerUUID = dbm.getMostKillsUUID();
			if (Bukkit.getPlayer(mostPlayerUUID) != null) {
				hologram.setCustomName("Most Kills: " + Bukkit.getPlayer(mostPlayerUUID).getDisplayName() + " has "
						+ dbm.getKills(mostPlayerUUID) + " kills");
			} else {
				hologram.setCustomName("Most Kills: " + Bukkit.getOfflinePlayer(mostPlayerUUID).getName() + " has "
						+ dbm.getKills(mostPlayerUUID) + " kills");
			}
		} else {
			String mostPlayerUUIDString = cr.getMostKillsUUID();
			if (mostPlayerUUIDString.length() != 0) {
				UUID mostPlayerUUID = UUID.fromString(
						mostPlayerUUIDString.substring(0, 9) + "-" + mostPlayerUUIDString.substring(9, 13) + "-"
								+ mostPlayerUUIDString.substring(13, 17) + "-" + mostPlayerUUIDString.substring(17, 21)
								+ "-" + mostPlayerUUIDString.substring(21));
				if (Bukkit.getPlayer(mostPlayerUUID) != null) {
					hologram.setCustomName("Most Kills: " + Bukkit.getPlayer(mostPlayerUUID).getDisplayName() + " has "
							+ cr.getKills(Bukkit.getPlayer(mostPlayerUUID).getUniqueId()) + " kills");
				} else {
					hologram.setCustomName("Most Kills: " + Bukkit.getOfflinePlayer(mostPlayerUUID).getName() + " has "
							+ cr.getKills(mostPlayerUUID) + " kills");
				}
			} else {
				hologram.setCustomName(
						"Most Kills: Insufficient data...");
			}
		}
	}

	@Deprecated
	public void mostDeaths(Location loc) {
		ArmorStand hologram = (ArmorStand) loc.getWorld().spawnEntity(deathsLoc, EntityType.ARMOR_STAND);
		hologram.setVisible(false);
		hologram.setGravity(false);
		hologram.setCustomNameVisible(true);

		if (!Main.isRedisEnabled()) {
			UUID mostPlayerUUID = dbm.getMostDeathsUUID();
			if (Bukkit.getPlayer(mostPlayerUUID) != null) {
				hologram.setCustomName("Most Deaths: " + Bukkit.getPlayer(mostPlayerUUID).getDisplayName() + " has "
						+ dbm.getDeaths(mostPlayerUUID) + " deaths");
			} else {
				hologram.setCustomName("Most Deaths: " + Bukkit.getOfflinePlayer(mostPlayerUUID).getName() + " has "
						+ dbm.getDeaths(mostPlayerUUID) + " deaths");
			}
		} else {
			String mostPlayerUUIDString = cr.getMostDeathsUUID();
			if (mostPlayerUUIDString.length() != 0) {
				UUID mostPlayerUUID = UUID.fromString(
						mostPlayerUUIDString.substring(0, 9) + "-" + mostPlayerUUIDString.substring(9, 13) + "-"
								+ mostPlayerUUIDString.substring(13, 17) + "-" + mostPlayerUUIDString.substring(17, 21)
								+ "-" + mostPlayerUUIDString.substring(21));
				if (Bukkit.getPlayer(mostPlayerUUID) != null) {
					hologram.setCustomName("Most Deaths: " + Bukkit.getPlayer(mostPlayerUUID).getDisplayName() + " has "
							+ cr.getDeaths(Bukkit.getPlayer(mostPlayerUUID).getUniqueId()) + " deaths");
				} else {
					hologram.setCustomName(
							"Most Deaths: " + Bukkit.getOfflinePlayer(mostPlayerUUID).getName() + " has "
									+ cr.getDeaths(mostPlayerUUID) + " deaths");
				}
			} else {
				hologram.setCustomName(
						"Most Deaths: Insufficient data...");
			}
		}
	}

	@Deprecated
	public void mostWins(Location loc) {
		ArmorStand hologram = (ArmorStand) loc.getWorld().spawnEntity(winsLoc, EntityType.ARMOR_STAND);
		hologram.setVisible(false);
		hologram.setGravity(false);
		hologram.setCustomNameVisible(true);

		if (!Main.isRedisEnabled()) {
			UUID mostPlayerUUID = dbm.getMostDeathsUUID();
			if (Bukkit.getPlayer(mostPlayerUUID) != null) {
				hologram.setCustomName("Most Wins: " + Bukkit.getPlayer(mostPlayerUUID).getDisplayName() + " has "
						+ dbm.getWins(mostPlayerUUID) + " wins");
			} else {
				hologram.setCustomName("Most Wins: " + Bukkit.getOfflinePlayer(mostPlayerUUID).getName() + " has "
						+ dbm.getWins(mostPlayerUUID) + " wins");
			}
		} else {
			// String mostPlayerUUIDString = cr.getMostWinsUUID();
			// if (mostPlayerUUIDString.length() != 0) {
			// 	UUID mostPlayerUUID = UUID.fromString(
			// 			mostPlayerUUIDString.substring(0, 9) + "-" + mostPlayerUUIDString.substring(9, 13) + "-"
			// 					+ mostPlayerUUIDString.substring(13, 17) + "-" + mostPlayerUUIDString.substring(17, 21)
			// 					+ "-" + mostPlayerUUIDString.substring(21));
			// 	if (Bukkit.getPlayer(mostPlayerUUID) != null) {
			// 		hologram.setCustomName("Most Wins: " + Bukkit.getPlayer(mostPlayerUUID).getDisplayName() + " has "
			// 				+ cr.getWins(Bukkit.getPlayer(mostPlayerUUID).getUniqueId()) + " wins");
			// 	} else {
			// 		hologram.setCustomName(
			// 				"Most Wins: " + Bukkit.getOfflinePlayer(mostPlayerUUID).getName() + " has "
			// 						+ cr.getWins(mostPlayerUUID) + " wins");
			// 	}
			// } else {
			// 	hologram.setCustomName("Most Wins: Insufficient data...");
			// }
		}
	}

}