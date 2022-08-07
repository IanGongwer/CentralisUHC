package com.iangongwer.listeners;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.iangongwer.game.GameManager;
import com.iangongwer.game.GameState;
import com.iangongwer.utils.Util;

public class DiamondAlert implements Listener {

    Util u = Util.getInstance();
    GameManager gm = GameManager.getInstance();

    @EventHandler
    public void onDiamondOreBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        if (GameState.isInGame()) {
            if (event.getBlock().getType().equals(Material.DIAMOND_ORE)) {
                for (UUID staffUUID : Util.getStaffMode()) {
                    Bukkit.getPlayer(staffUUID)
                            .sendMessage(u.messageFormat("[UHC] " + player.getDisplayName() + " mined a diamond", "a"));
                }
            }
        }
    }

}