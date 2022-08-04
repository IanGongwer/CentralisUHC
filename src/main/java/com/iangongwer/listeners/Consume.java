package com.iangongwer.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Consume implements Listener {

	@EventHandler
	public void onGoldenAppleEat(PlayerItemConsumeEvent event) {
		Player player = event.getPlayer();
		if (event.getItem().getItemMeta().getDisplayName() != null) {
			if (event.getItem().getItemMeta().getDisplayName().equals("Golden Head")) {

				PotionEffect speed = new PotionEffect(PotionEffectType.SPEED, 100, 0);
				PotionEffect strength = new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 100, 0);
				PotionEffect regeneration = new PotionEffect(PotionEffectType.REGENERATION, 100, 1);

				player.addPotionEffect(speed);
				player.addPotionEffect(strength);
				player.addPotionEffect(regeneration);
			}
		}
	}

}