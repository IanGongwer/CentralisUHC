package com.iangongwer.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.iangongwer.utils.ChatUtil;
import com.iangongwer.utils.Util;

public class PlayerAndChatMuted implements Listener {

    Util u = Util.getInstance();

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if (ChatUtil.isMuted(player.getUniqueId())) {
            if (!ChatUtil.isChatMuted()) {
                player.sendMessage(u.messageFormat("You are currently muted", "c"));
                event.setCancelled(true);
                return;
            }
        } else if (ChatUtil.isChatMuted() && !player.isOp() && !player.hasPermission("uhc.staff")) {
            player.sendMessage(u.messageFormat("The chat is currently muted", "c"));
            event.setCancelled(true);
            return;
        } else if (event.getMessage().contains("%")) {
            Bukkit.broadcastMessage(ChatUtil.chatMessage(event.getMessage(), player));
            event.setCancelled(true);
            return;
        } else {
            event.setFormat(ChatUtil.chatMessage(event.getMessage(), player));
            event.setCancelled(false);
            return;
        }
    }

}