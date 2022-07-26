package com.iangongwer.utils;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;

import com.iangongwer.main.Main;

public class YMLFile {

	Main main = Main.getInstance();

	public YMLFile() {
		File file = new File(main.getDataFolder(), "config.yml");
		if (!file.exists()) {
			getFileConfig().addDefault("Configuration.Host", "localhost");
			getFileConfig().addDefault("Configuration.Port", 3306);
			getFileConfig().addDefault("Configuration.Database", "uhc");
			getFileConfig().addDefault("Configuration.Username", "tutorial");
			getFileConfig().addDefault("Configuration.Password", "tutorial");
			getFileConfig().options().copyDefaults();
			main.saveDefaultConfig();
		}
	}

	public FileConfiguration getFileConfig() {
		return main.getConfig();
	}
}