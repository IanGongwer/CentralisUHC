package com.iangongwer.utils;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;

import com.iangongwer.main.Main;

public class YMLFile {

	Main main = Main.getInstance();

	FileConfiguration config = main.getConfig();

	public YMLFile() {
		File file = new File(main.getDataFolder(), "config.yml");
		if (!file.exists()) {
			config.addDefault("Configuration.Host", "localhost");
			config.addDefault("Configuration.Port", 3306);
			config.addDefault("Configuration.Database", "uhc");
			config.addDefault("Configuration.Username", "tutorial");
			config.addDefault("Configuration.Password", "tutorial");
			config.options().copyDefaults();
			main.saveDefaultConfig();
		}
	}

	public FileConfiguration getFileConfig() {
		return main.getConfig();
	}
}