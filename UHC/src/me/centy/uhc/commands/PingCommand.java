package me.centy.uhc.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import me.centy.uhc.game.Util;
import net.minecraft.server.v1_8_R3.EntityPlayer;

public class PingCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) {
		Player player = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("ping")) {
			if (args.length > 1) {
				player.sendMessage(Util.getInstance().messageFormat("Usage: /ping (player)", "c"));
			}
			if (args.length == 0) {
				player.sendMessage(Util.getInstance().messageFormat("Your Ping: " + getPing(player), "a"));
			}
			if (args.length == 1) {
				Player target = Bukkit.getServer().getPlayer(args[0]);
				player.sendMessage(
						Util.getInstance().messageFormat(target.getDisplayName() + "'s Ping: " + getPing(target), "a"));
			}
		}
		return true;
	}

	public static int getPing(Player player) {
		CraftPlayer pingcraft = (CraftPlayer) player;
		EntityPlayer pingentity = pingcraft.getHandle();
		return pingentity.ping;
	}
}