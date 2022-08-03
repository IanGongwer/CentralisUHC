package com.iangongwer.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.iangongwer.utils.ChatUtil;
import com.iangongwer.utils.Util;

public class Chat implements Listener {

	Util u = Util.getInstance();

	@EventHandler(priority = EventPriority.MONITOR)
	public void handleASyncPlayerChatEvent(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		if (ChatUtil.isChatMuted()) {
			if (!player.hasPermission("uhc.staff")) {
				player.sendMessage(u.messageFormat("The chat is currently muted.", "c"));
				event.setCancelled(true);
			} else {
				if (event.getMessage().contains("%")) {
					Bukkit.broadcastMessage(ChatUtil.chatMessage(event.getMessage(), player));
					event.setCancelled(true);
				} else {
					event.setFormat(ChatUtil.chatMessage(event.getMessage(), player));
				}
			}
		} else {
			if (ChatUtil.isMuted(player.getUniqueId())) {
				player.sendMessage(u.messageFormat("You are currently muted.", "c"));
				event.setCancelled(true);
			} else {
				if (event.getMessage().contains("%")) {
					Bukkit.broadcastMessage(ChatUtil.chatMessage(event.getMessage(), player));
					event.setCancelled(true);
				} else {
					event.setFormat(ChatUtil.chatMessage(event.getMessage(), player));
				}
			}
		}

	}
}