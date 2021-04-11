package me.centy.uhc.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

public class Weather implements Listener {

	@EventHandler
	public void onWeatherChange(WeatherChangeEvent event) {
		event.setCancelled(true);
	}

}