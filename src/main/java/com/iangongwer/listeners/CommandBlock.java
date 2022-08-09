package com.iangongwer.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandBlock implements Listener {

	@EventHandler
	public void onCommandSend(PlayerCommandPreprocessEvent event) {
		if (!event.getPlayer().isOp() && !event.getPlayer().hasPermission("uhc.staff")) {
			if (event.getMessage().startsWith("/me") || event.getMessage().startsWith("/ver")
					|| event.getMessage().startsWith("/pl") || event.getMessage().startsWith("/worldedit")
					|| event.getMessage().startsWith("/we") || event.getMessage().startsWith("/minecraft:")
					|| event.getMessage().startsWith("/bukkit:")
					|| event.getMessage().startsWith("/tc") || event.getMessage().startsWith("/wb")
					|| event.getMessage().startsWith("/calc") || event.getMessage().startsWith("/searchitem")
					|| event.getMessage().startsWith("/sel") || event.getMessage().startsWith("/toggleplace")
					|| event.getMessage().startsWith("/none") || event.getMessage().startsWith("/tool")
					|| event.getMessage().startsWith("/lp")) {
				event.setCancelled(true);
			}
		}
	}

}