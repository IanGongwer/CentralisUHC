package com.iangongwer.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class Weather implements Listener {

	@EventHandler
	public void onWeatherChange(WeatherChangeEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void onThunderChange(ThunderChangeEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void onLightingStrike(LightningStrikeEvent event) {
		event.setCancelled(true);
	}

}