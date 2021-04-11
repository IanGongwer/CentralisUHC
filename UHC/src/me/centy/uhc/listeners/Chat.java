package me.centy.uhc.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.centy.uhc.game.Util;

public class Chat implements Listener {

	@EventHandler(priority = EventPriority.MONITOR)
	public void handleASyncPlayerChatEvent(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		if (Util.getInstance().isChatMuted()) {
			if (!player.hasPermission("uhc.staff")) {
				player.sendMessage(Util.getInstance().messageFormat("The chat is currently muted.", "c"));
				event.setCancelled(true);
			} else {
				if (event.getMessage().contains("%")) {
					Bukkit.broadcastMessage(Util.getInstance().chatMessage(event.getMessage(), player));
					event.setCancelled(true);
				} else {
					event.setFormat(Util.getInstance().chatMessage(event.getMessage(), player));
				}
			}
		} else {
			if (Util.getInstance().isMuted(player.getUniqueId())) {
				player.sendMessage(Util.getInstance().messageFormat("You are currently muted.", "c"));
				event.setCancelled(true);
			} else {
				if (event.getMessage().contains("%")) {
					Bukkit.broadcastMessage(Util.getInstance().chatMessage(event.getMessage(), player));
					event.setCancelled(true);
				} else {
					event.setFormat(Util.getInstance().chatMessage(event.getMessage(), player));
				}
			}
		}

	}
}