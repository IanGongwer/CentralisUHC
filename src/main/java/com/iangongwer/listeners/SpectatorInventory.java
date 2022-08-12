package com.iangongwer.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import com.iangongwer.utils.InventoryUtil;
import com.iangongwer.utils.Util;

public class SpectatorInventory implements Listener {

    @EventHandler
    public void onPlayerClickPlayer(PlayerInteractAtEntityEvent event) {
        Player staffMember = event.getPlayer();
        if (event.getRightClicked() instanceof Player) {
            Player player = (Player) event.getRightClicked();
            if (Util.getInstance().isInStaffMode(staffMember.getUniqueId())) {
                InventoryUtil.createSpectatorInventory(staffMember, player);
            }
        }
    }

}