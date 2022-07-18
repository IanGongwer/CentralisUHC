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

	public void createLobbyHologram() {
		for (Entity entity : Bukkit.getWorld("world").getEntities()) {
			if (entity.getType().equals(EntityType.ARMOR_STAND)) {
				entity.remove();
			}
		}

		// First Line
		ArmorStand hologram = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
		hologram.setVisible(false);
		hologram.setGravity(false);
		hologram.setCustomNameVisible(true);
		hologram.setCustomName("Server Statistics");
		mostKills(loc);
		mostDeaths(loc);
		mostWins(loc);
	}

	public void mostKills(Location loc) {
		ArmorStand hologram = (ArmorStand) loc.getWorld().spawnEntity(loc.add(0, -.35, 0), EntityType.ARMOR_STAND);
		hologram.setVisible(false);
		hologram.setGravity(false);
		hologram.setCustomNameVisible(true);

		if(!Main.isRedisEnabled()) {
			UUID mostPlayerUUID = dbm.getMostKillsUUID();
			if(Bukkit.getPlayer(mostPlayerUUID) != null) {
				hologram.setCustomName("Most Kills: " + Bukkit.getPlayer(mostPlayerUUID).getDisplayName() + " has "
				+ dbm.getKills(mostPlayerUUID) + " kills");
			} else {
				hologram.setCustomName("Most Kills: " + Bukkit.getOfflinePlayer(mostPlayerUUID).getName() + " has "
				+ dbm.getKills(mostPlayerUUID) + " kills");
			}
		} else {
			String mostPlayerUUID = cr.getMostKillsUUID();
			if(Bukkit.getPlayer(mostPlayerUUID) != null) {
				hologram.setCustomName("Most Kills: " + Bukkit.getPlayer(mostPlayerUUID).getDisplayName() + " has "
				+ cr.getKills(Bukkit.getPlayer(mostPlayerUUID).getUniqueId()) + " kills");
			} else {
				hologram.setCustomName("Most Kills: " + Bukkit.getOfflinePlayer(UUID.fromString(mostPlayerUUID)).getName() + " has "
				+ cr.getKills(UUID.fromString(mostPlayerUUID)) + " kills");
			}
		}
	}

	public void mostDeaths(Location loc) {
		ArmorStand hologram = (ArmorStand) loc.getWorld().spawnEntity(loc.add(0, -.35, 0), EntityType.ARMOR_STAND);
		hologram.setVisible(false);
		hologram.setGravity(false);
		hologram.setCustomNameVisible(true);

		if(!Main.isRedisEnabled()) {
			UUID mostPlayerUUID = dbm.getMostDeathsUUID();
			if(Bukkit.getPlayer(mostPlayerUUID) != null) {
			hologram.setCustomName("Most Deaths: " + Bukkit.getPlayer(mostPlayerUUID).getDisplayName() + " has "
					+ dbm.getDeaths(mostPlayerUUID) + " deaths");
			} else {
				hologram.setCustomName("Most Deaths: " + Bukkit.getOfflinePlayer(mostPlayerUUID).getName() + " has "
				+ dbm.getDeaths(mostPlayerUUID) + " deaths");
			}
		} else {
			String mostPlayerUUID = cr.getMostDeathsUUID();
			if(Bukkit.getPlayer(mostPlayerUUID) != null) {
				hologram.setCustomName("Most Deaths: " + Bukkit.getPlayer(mostPlayerUUID).getDisplayName() + " has "
				+ cr.getDeaths(Bukkit.getPlayer(mostPlayerUUID).getUniqueId()) + " deaths");
			} else {
				hologram.setCustomName("Most Deaths: " + Bukkit.getOfflinePlayer(UUID.fromString(mostPlayerUUID)).getName() + " has "
				+ cr.getDeaths(UUID.fromString(mostPlayerUUID)) + " deaths");
			}
		}
	}

	public void mostWins(Location loc) {
		ArmorStand hologram = (ArmorStand) loc.getWorld().spawnEntity(loc.add(0, -.35, 0), EntityType.ARMOR_STAND);
		hologram.setVisible(false);
		hologram.setGravity(false);
		hologram.setCustomNameVisible(true);

		if(!Main.isRedisEnabled()) {
			UUID mostPlayerUUID = dbm.getMostDeathsUUID();
			if(Bukkit.getPlayer(mostPlayerUUID) != null) {
				hologram.setCustomName("Most Wins: " + Bukkit.getPlayer(mostPlayerUUID).getDisplayName() + " has "
						+ dbm.getWins(mostPlayerUUID) + " wins");
			} else {
				hologram.setCustomName("Most Wins: " + Bukkit.getOfflinePlayer(mostPlayerUUID).getName() + " has "
				+ dbm.getWins(mostPlayerUUID) + " wins");
			}
		} else {
			String mostPlayerUUID = cr.getMostDeathsUUID();
			if(Bukkit.getPlayer(mostPlayerUUID) != null) {
				hologram.setCustomName("Most Wins: " + Bukkit.getPlayer(mostPlayerUUID).getDisplayName() + " has "
				+ cr.getWins(Bukkit.getPlayer(mostPlayerUUID).getUniqueId()) + " wins");
			} else {
				hologram.setCustomName("Most Wins: " + Bukkit.getOfflinePlayer(UUID.fromString(mostPlayerUUID)).getName() + " has "
				+ cr.getWins(UUID.fromString(mostPlayerUUID)) + " wins");
			}
		}
	}

}