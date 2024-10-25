package de.doppelkool.knockit;

import de.doppelkool.knockit.DatabaseCommunication.ConfigurationValueRepository;
import de.doppelkool.knockit.DatabaseCommunication.LocationRepository;
import de.doppelkool.knockit.DatabaseCommunication.PlayerStatsRepository;
import de.doppelkool.knockit.DatabaseCommunication.Repository;
import de.doppelkool.knockit.commands.*;
import de.doppelkool.knockit.errorhandling.ErrorHandler;
import de.doppelkool.knockit.listener.*;
import de.doppelkool.knockit.service.GameDesignService;
import de.doppelkool.knockit.service.SetupService;
import de.doppelkool.knockit.ConfigHandling.ConfigHandler;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class Description
 *
 * @author doppelkool | github.com/doppelkool
 */
@NoArgsConstructor
public class Main extends JavaPlugin {

	@Getter
	private static Plugin plugin;

	@Getter
	private static PluginManager pluginManager;

	@Getter
	private static ConfigHandler configHandler;

	@Override
	public void onEnable() {
		plugin = this;
		pluginManager = getServer().getPluginManager();

		prepareConfig();
		prepareDB();
		prepareRepositorys();

		if (SetupService.getInstance().isSetup()) {
			ConfigurationValueRepository.getInstance().update(ConfigurationValueRepository.DBConfigValueNames.setupFinished, true);
			registerGameEvents();
		} else {
			registerSetupEvents();
		}

		registerCommands();

		plugin.getServer().getLogger().info(ErrorHandler.PLUGIN_PREFIX + ChatColor.GREEN + "Plugin erfolgreich geladen");
	}

	@Override
	public void onDisable() {

	}

	private void prepareConfig() {
		configHandler = new ConfigHandler();
	}

	private void prepareDB() {
		try (Connection connection = Repository.getSetupConnection();
		     Statement statement = connection.createStatement();
		     InputStream inputStream = getResource("knockit.sql");
		     BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

			StringBuilder sql = new StringBuilder();
			String line;

			while ((line = reader.readLine()) != null) {
				sql.append(line);
				sql.append(System.lineSeparator());
			}

			String[] statements = sql.toString().split(";");
			for (String stmt : statements) {

				stmt = stmt
					.trim()
					//PLACEHOLDERS
					.replace("%DATABASE_NAME%", ConfigHandler.MYSQL_DATABASE);
				Bukkit.getLogger().info(stmt);
				if (!stmt.isEmpty()) {
					statement.execute(stmt);
				}
			}

			Bukkit.getLogger().info(ErrorHandler.PLUGIN_PREFIX + "Database Schema prepared successfully");
		} catch (SQLException e) {
			System.err.println("SQL Exception: " + e.getMessage());
		} catch (IOException e) {
			System.err.println("IO Exception: " + e.getMessage());
		}
	}

	private void prepareRepositorys() {
		new LocationRepository();
		new PlayerStatsRepository();
		new ConfigurationValueRepository();
	}

	private void registerSetupEvents() {
		pluginManager.registerEvents(SetupListeners.setupJoinListener, plugin);
	}

	private void registerGameEvents() {
		pluginManager.registerEvents(GameListeners.gameJoinListener, plugin);
		pluginManager.registerEvents(GameListeners.fallDamageListener, plugin);
		pluginManager.registerEvents(GameListeners.onDeathListener, plugin);
		pluginManager.registerEvents(GameListeners.onMoveListener, plugin);
	}

	private void registerCommands() {
		getCommand("stats").setExecutor(new StatsCommand());
		getCommand("setspawn").setExecutor(new SetSpawnCommand());
		getCommand("setheight").setExecutor(new SetHeightCommand());
		getCommand("finish").setExecutor(new FinishCommand());
	}

	public static class SetupListeners {
		public static final SetupJoinListener setupJoinListener = new SetupJoinListener();
	}

	public static class GameListeners {
		public static final GameJoinListener gameJoinListener = new GameJoinListener();
		public static final FallDamageListener fallDamageListener = new FallDamageListener();
		public static final OnDeathListener onDeathListener = new OnDeathListener();
		public static final OnMoveListener onMoveListener = new OnMoveListener();
	}
}