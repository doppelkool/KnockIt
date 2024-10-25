package de.doppelkool.knockit;

import de.doppelkool.knockit.DatabaseCommunication.ConfigurationValueRepository;
import de.doppelkool.knockit.DatabaseCommunication.LocationRepository;
import de.doppelkool.knockit.DatabaseCommunication.PlayerStatsRepository;
import de.doppelkool.knockit.DatabaseCommunication.Repository;
import de.doppelkool.knockit.commands.FinishCommand;
import de.doppelkool.knockit.commands.SetHeightCommand;
import de.doppelkool.knockit.commands.SetSpawnCommand;
import de.doppelkool.knockit.commands.StatsCommand;
import de.doppelkool.knockit.errorhandling.ErrorHandler;
import de.doppelkool.knockit.listener.FallDamageListener;
import de.doppelkool.knockit.listener.GameJoinListener;
import de.doppelkool.knockit.listener.SetupJoinListener;
import de.doppelkool.knockit.service.GameDesignService;
import de.doppelkool.knockit.service.SetupService;
import de.doppelkool.knockit.ConfigHandling.ConfigHandler;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
		Bukkit.getLogger().info("KnockIt - DBZero");
		try (Connection connection = Repository.getConnection();
		     Statement statement = connection.createStatement();
		     InputStream inputStream = getResource("knockit.sql");
		     BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
			Bukkit.getLogger().info("KnockIt - DBFirst");

			StringBuilder sql = new StringBuilder();
			String line;

			// Read the SQL file line by line
			while ((line = reader.readLine()) != null) {
				// Append the line to the SQL statement
				sql.append(line);
				sql.append(System.lineSeparator());
			}
			Bukkit.getLogger().info("KnockIt - AfterAppend");

			// Execute the SQL statements
			String[] statements = sql.toString().split(";"); // Split by semicolon
			for (String stmt : statements) {
				Bukkit.getLogger().info("KnockIt - Statement: " + stmt);
				stmt = stmt.trim(); // Trim whitespace
				if (!stmt.isEmpty()) {
					statement.execute(stmt); // Execute the SQL statement
					Bukkit.getLogger().info("KnockIt - After execute");
				}
			}

			System.out.println("SQL file executed successfully!");

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
		pluginManager.registerEvents(new SetupJoinListener(), plugin);
	}

	private void registerGameEvents() {
		pluginManager.registerEvents(new GameJoinListener(), plugin);
		pluginManager.registerEvents(new FallDamageListener(), plugin);
	}

	private void registerCommands() {
		getCommand("stats").setExecutor(new StatsCommand());
		getCommand("setspawn").setExecutor(new SetSpawnCommand());
		getCommand("setheight").setExecutor(new SetHeightCommand());
		getCommand("finish").setExecutor(new FinishCommand());
	}
}