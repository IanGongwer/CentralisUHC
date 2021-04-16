package me.centy.uhc.main;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import me.centy.uhc.commands.BroadcastCommand;
import me.centy.uhc.commands.CalculateCommand;
import me.centy.uhc.commands.LateScatterCommand;
import me.centy.uhc.commands.MuteChatCommand;
import me.centy.uhc.commands.MuteCommand;
import me.centy.uhc.commands.PingCommand;
import me.centy.uhc.commands.PracticeCommand;
import me.centy.uhc.commands.ScenarioCommand;
import me.centy.uhc.commands.StaffCommand;
import me.centy.uhc.commands.StartCommand;
import me.centy.uhc.commands.WhitelistCommand;
import me.centy.uhc.game.GameState;
import me.centy.uhc.game.Util;
import me.centy.uhc.listeners.Break;
import me.centy.uhc.listeners.Chat;
import me.centy.uhc.listeners.CombatLogDeath;
import me.centy.uhc.listeners.CommandBlock;
import me.centy.uhc.listeners.Connect;
import me.centy.uhc.listeners.Consume;
import me.centy.uhc.listeners.Death;
import me.centy.uhc.listeners.Drop;
import me.centy.uhc.listeners.EntitySpawning;
import me.centy.uhc.listeners.Fall;
import me.centy.uhc.listeners.FoodLevel;
import me.centy.uhc.listeners.Interact;
import me.centy.uhc.listeners.Join;
import me.centy.uhc.listeners.Place;
import me.centy.uhc.listeners.PvP;
import me.centy.uhc.listeners.Quit;
import me.centy.uhc.listeners.Respawn;
import me.centy.uhc.listeners.Weather;
import me.centy.uhc.runnables.CombatLogRunnable;
import me.centy.uhc.runnables.EndRunnable;
import me.centy.uhc.runnables.GameRunnable;
import me.centy.uhc.scenarios.CutCleanandBD;
import me.centy.uhc.scenarios.HasteyBoys;
import me.centy.uhc.scenarios.Timber;

public class Main extends JavaPlugin {

	private static Main instance;

	@SuppressWarnings("deprecation")
	public void registerRunnables() {
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new CombatLogRunnable(), 0L, 20L);
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new GameRunnable(), 0L, 20L);
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new EndRunnable(), 0L, 20L);
	}

	public void registerListeners() {
		getServer().getPluginManager().registerEvents(new Break(), this);
		getServer().getPluginManager().registerEvents(new Chat(), this);
		getServer().getPluginManager().registerEvents(new CombatLogDeath(), this);
		getServer().getPluginManager().registerEvents(new CommandBlock(), this);
		getServer().getPluginManager().registerEvents(new Connect(), this);
		getServer().getPluginManager().registerEvents(new Consume(), this);
		getServer().getPluginManager().registerEvents(new Death(), this);
		getServer().getPluginManager().registerEvents(new Drop(), this);
		getServer().getPluginManager().registerEvents(new EntitySpawning(), this);
		getServer().getPluginManager().registerEvents(new Fall(), this);
		getServer().getPluginManager().registerEvents(new FoodLevel(), this);
		getServer().getPluginManager().registerEvents(new Interact(), this);
		getServer().getPluginManager().registerEvents(new Join(), this);
		getServer().getPluginManager().registerEvents(new Place(), this);
		getServer().getPluginManager().registerEvents(new PvP(), this);
		getServer().getPluginManager().registerEvents(new Quit(), this);
		getServer().getPluginManager().registerEvents(new Respawn(), this);
		getServer().getPluginManager().registerEvents(new Weather(), this);
	}

	public void registerScenarios() {
		getServer().getPluginManager().registerEvents(new CutCleanandBD(), this);
		getServer().getPluginManager().registerEvents(new HasteyBoys(), this);
		getServer().getPluginManager().registerEvents(new Timber(), this);
	}

	public void registerCommands() {
		getCommand("broadcast").setExecutor((CommandExecutor) new BroadcastCommand());
		getCommand("calculate").setExecutor((CommandExecutor) new CalculateCommand());
		getCommand("latescatter").setExecutor((CommandExecutor) new LateScatterCommand());
		getCommand("mutechat").setExecutor((CommandExecutor) new MuteChatCommand());
		getCommand("mute").setExecutor((CommandExecutor) new MuteCommand());
		getCommand("ping").setExecutor((CommandExecutor) new PingCommand());
		getCommand("practice").setExecutor((CommandExecutor) new PracticeCommand());
		getCommand("scenario").setExecutor((CommandExecutor) new ScenarioCommand());
		getCommand("staff").setExecutor((CommandExecutor) new StaffCommand());
		getCommand("start").setExecutor((CommandExecutor) new StartCommand());
		getCommand("whitelist").setExecutor((CommandExecutor) new WhitelistCommand());
	}

	public void onEnable() {
		registerRunnables();
		registerListeners();
		registerScenarios();
		registerCommands();
		GameState.setState(GameState.Lobby);
		Bukkit.getWorld("world").setGameRuleValue("doDaylightCycle", "false");
		Bukkit.getWorld("world").setTime(6000);
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "butcher");
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "butcher -a");
		Util.getInstance().createWorld("uhc_world");
		if (Bukkit.getWorld("uhc_world") != null) {
			Bukkit.getWorld("uhc_world").setGameRuleValue("naturalRegeneration", "false");
			Bukkit.getWorld("uhc_world").setGameRuleValue("doDaylightCycle", "false");
			Bukkit.getWorld("uhc_world").setTime(6000);
		}
	}

	public void onDisable() {

	}

	public static Main getInstance() {
		return instance;
	}

}