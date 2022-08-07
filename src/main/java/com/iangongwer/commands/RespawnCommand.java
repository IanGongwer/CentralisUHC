package com.iangongwer.commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.iangongwer.game.GameManager;
import com.iangongwer.team.TeamManager;
import com.iangongwer.utils.Util;

public class RespawnCommand implements CommandExecutor {

	Util u = Util.getInstance();
	GameManager gm = GameManager.getInstance();

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("respawn") && sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length == 1) {
				if (Bukkit.getPlayer(args[0]) != null) {
					Player respawnPlayer = Bukkit.getPlayer(args[0]);
					if (gm.isSpectator(respawnPlayer.getUniqueId())) {
						for (ItemStack item : gm.getDeathInventory(respawnPlayer.getUniqueId())) {
							respawnPlayer.getInventory().addItem(item);
						}
						TeamManager.getInstance().getDeceasedMembers(player.getUniqueId()).remove(player.getUniqueId());
						gm.addPlayer(respawnPlayer.getUniqueId());
						gm.removeSpectator(respawnPlayer.getUniqueId());
						respawnPlayer.setGameMode(GameMode.SURVIVAL);
						respawnPlayer.teleport(gm.getDeathLocations().get(respawnPlayer.getUniqueId()));
						player.sendMessage(
								u.messageFormat("You have respawned " + respawnPlayer.getDisplayName() + ".", "a"));
						respawnPlayer.sendMessage(u.messageFormat("You have been respawned, continue to play.", "a"));
					} else {
						player.sendMessage(u.messageFormat(
								respawnPlayer.getDisplayName() + " is already a player and in-game currently.", "c"));
					}
				} else {
					player.sendMessage(u.messageFormat(args[0] + " is not currently online.", "c"));
				}
			} else {
				player.sendMessage(u.messageFormat("/respawn <player>", "c"));
			}
		}
		return true;
	}
}