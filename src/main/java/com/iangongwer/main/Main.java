package com.iangongwer.main;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import com.iangongwer.commands.BroadcastCommand;
import com.iangongwer.commands.CalculateCommand;
import com.iangongwer.commands.DatabaseCommand;
import com.iangongwer.commands.LateScatterCommand;
import com.iangongwer.commands.MuteChatCommand;
import com.iangongwer.commands.MuteCommand;
import com.iangongwer.commands.PracticeCommand;
import com.iangongwer.commands.RespawnCommand;
import com.iangongwer.commands.ScenarioCommand;
import com.iangongwer.commands.StaffCommand;
import com.iangongwer.commands.StartCommand;
import com.iangongwer.commands.StatsCommand;
import com.iangongwer.commands.TeamChatCommand;
import com.iangongwer.commands.TeamCommand;
import com.iangongwer.commands.TeamCoordsCommand;
import com.iangongwer.commands.WhitelistCommand;
import com.iangongwer.game.GameState;
import com.iangongwer.listeners.Break;
import com.iangongwer.listeners.Chat;
import com.iangongwer.listeners.CommandBlock;
import com.iangongwer.listeners.Connect;
import com.iangongwer.listeners.Consume;
import com.iangongwer.listeners.Death;
import com.iangongwer.listeners.Drop;
import com.iangongwer.listeners.EntitySpawning;
import com.iangongwer.listeners.Fall;
import com.iangongwer.listeners.FoodLevel;
import com.iangongwer.listeners.GodAppleCraft;
import com.iangongwer.listeners.Interact;
import com.iangongwer.listeners.Join;
import com.iangongwer.listeners.Place;
import com.iangongwer.listeners.PvP;
import com.iangongwer.listeners.Quit;
import com.iangongwer.listeners.Respawn;
import com.iangongwer.listeners.Sheep;
import com.iangongwer.listeners.VillagerDeath;
import com.iangongwer.listeners.Weather;
import com.iangongwer.mysql.ConnectionMYSQL;
import com.iangongwer.redis.ConnectionRedis;
import com.iangongwer.runnables.EndRunnable;
import com.iangongwer.runnables.GameRunnable;
import com.iangongwer.runnables.QuitLogRunnable;
import com.iangongwer.scenarios.Bowless;
import com.iangongwer.scenarios.CutCleanandBD;
import com.iangongwer.scenarios.HasteyBoys;
import com.iangongwer.scenarios.Horseless;
import com.iangongwer.scenarios.Timber;
import com.iangongwer.utils.Util;
import com.iangongwer.utils.YMLFile;

public class Main extends JavaPlugin {

	private static Main instance;

	private static boolean redisEnabled = false;

	@SuppressWarnings("deprecation")
	public void registerRunnables() {
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new QuitLogRunnable(), 0L, 40L);
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new GameRunnable(), 0L, 20L);
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new EndRunnable(), 0L, 20L);
	}

	public void registerListeners() {
		getServer().getPluginManager().registerEvents(new Break(), this);
		getServer().getPluginManager().registerEvents(new Chat(), this);
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
		getServer().getPluginManager().registerEvents(new VillagerDeath(), this);
		getServer().getPluginManager().registerEvents(new Sheep(), this);
		getServer().getPluginManager().registerEvents(new GodAppleCraft(), this);
	}

	public void registerScenarios() {
		getServer().getPluginManager().registerEvents(new CutCleanandBD(), this);
		getServer().getPluginManager().registerEvents(new HasteyBoys(), this);
		getServer().getPluginManager().registerEvents(new Timber(), this);
		getServer().getPluginManager().registerEvents(new Bowless(), this);
		getServer().getPluginManager().registerEvents(new Horseless(), this);
	}

	public void registerCommands() {
		getCommand("team").setExecutor((CommandExecutor) new TeamCommand());
		getCommand("respawn").setExecutor((CommandExecutor) new RespawnCommand());
		getCommand("teamchat").setExecutor((CommandExecutor) new TeamChatCommand());
		getCommand("teamcoords").setExecutor((CommandExecutor) new TeamCoordsCommand());
		getCommand("broadcast").setExecutor((CommandExecutor) new BroadcastCommand());
		getCommand("calculate").setExecutor((CommandExecutor) new CalculateCommand());
		getCommand("latescatter").setExecutor((CommandExecutor) new LateScatterCommand());
		getCommand("mutechat").setExecutor((CommandExecutor) new MuteChatCommand());
		getCommand("mute").setExecutor((CommandExecutor) new MuteCommand());
		getCommand("practice").setExecutor((CommandExecutor) new PracticeCommand());
		getCommand("scenario").setExecutor((CommandExecutor) new ScenarioCommand());
		getCommand("staff").setExecutor((CommandExecutor) new StaffCommand());
		getCommand("start").setExecutor((CommandExecutor) new StartCommand());
		getCommand("whitelist").setExecutor((CommandExecutor) new WhitelistCommand());
		getCommand("stats").setExecutor((CommandExecutor) new StatsCommand());
		getCommand("database").setExecutor((CommandExecutor) new DatabaseCommand());
	}

	public void onEnable() {
		instance = this;
		new YMLFile();
		try {
			Main.setRedisEnabled(false);
			ConnectionMYSQL.getInstance().connect();
		} catch (Exception e) {
			Main.setRedisEnabled(true);
			ConnectionRedis.getInstance().connectToRedis();
		}
		// new LobbyHolograms().createLobbyHologram(); Not working correctly right now
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
		if(isRedisEnabled()) {
			ConnectionRedis.getInstance().closePool();
		}
	}

	public static Main getInstance() {
		return instance;
	}

	public static boolean isRedisEnabled() {
		return redisEnabled;
	}

	public static void setRedisEnabled(boolean clause) {
		redisEnabled = clause;
	}

}