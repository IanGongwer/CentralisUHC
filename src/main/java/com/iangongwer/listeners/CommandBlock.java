package com.iangongwer.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandBlock implements Listener {

	@EventHandler
	public void onCommandSend(PlayerCommandPreprocessEvent event) {
		if (event.getMessage().startsWith("/me") || event.getMessage().startsWith("/ver")
				|| event.getMessage().startsWith("/pl") || event.getMessage().startsWith("/worldedit")
				|| event.getMessage().startsWith("/we") || event.getMessage().startsWith("/minecraft:me")
				|| event.getMessage().startsWith("/bukkit:")) {
			event.setCancelled(true);
		}
	}

}