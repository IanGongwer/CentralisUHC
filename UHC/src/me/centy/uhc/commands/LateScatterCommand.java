package me.centy.uhc.commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.centy.uhc.game.GameManager;
import me.centy.uhc.game.Util;

public class LateScatterCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("latescatter") && sender instanceof Player) {
			Player player = (Player) sender;
			if (Util.getInstance().isInGame()) {
				if (!GameManager.getInstance().isPvPEnabled()) {
					if (!Util.getInstance().isInStaffMode(player.getUniqueId())) {
						if (!GameManager.getInstance().getPlayers().contains(player.getUniqueId())) {
							PotionEffect blindness = new PotionEffect(PotionEffectType.BLINDNESS, 10000, 1);
							player.getInventory().clear();
							player.getInventory().setArmorContents(null);
							player.addPotionEffect(blindness);
							player.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 16));
							Location scatterLocation = GameManager.getInstance().checkLocationEligibilityNoTeleport(
									GameManager.getInstance().makeScatterLocation());
							scatterLocation.setY(scatterLocation.getY() + 1);
							player.teleport(scatterLocation);
							player.removePotionEffect(PotionEffectType.BLINDNESS);
							player.setFoodLevel(20);
							player.setHealth(20.0);
							player.setGameMode(GameMode.SURVIVAL);
							GameManager.getInstance().setPlayerKills(player.getUniqueId(), 0);
							GameManager.getInstance().addPlayer(player.getUniqueId());
							GameManager.getInstance().removeSpectator(player.getUniqueId());
							Util.getInstance().createGameScoreboard(player);
							Bukkit.broadcastMessage(Util.getInstance()
									.messageFormat(player.getDisplayName() + " has been latescattered.", "a"));
							for (Player allPlayers : Bukkit.getOnlinePlayers()) {
								allPlayers.showPlayer(player);
							}
						} else {
							player.sendMessage(Util.getInstance().messageFormat("You are already scattered.", "c"));
						}
					} else {
						player.sendMessage(Util.getInstance()
								.messageFormat("You are in staff mode. Use /staff to remove staff mode.", "c"));
					}
				} else {
					player.sendMessage(
							Util.getInstance().messageFormat("The game is too far in progress to join.", "c"));
				}
			} else {
				player.sendMessage(Util.getInstance().messageFormat("The game is not in progress.", "c"));
			}
		}
		return true;
	}
}