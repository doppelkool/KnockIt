package de.doppelkool.knockit;

import de.doppelkool.knockit.commands.StatsCommand;
import de.doppelkool.knockit.errorhandling.ErrorHandler;
import de.doppelkool.knockit.listener.JoinListener;
import de.doppelkool.knockit.service.SetupService;
import de.doppelkool.knockit.storage.ConfigHandler;
import de.doppelkool.knockit.storage.MySQL;
import de.doppelkool.knockit.storage.MySQLHandler;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

/**
 * Class Description
 *
 * @author doppelkool | github.com/doppelkool
 */
public class Main extends JavaPlugin {
	@Getter
	private static Plugin plugin;

	private static PluginManager pluginManager;

	@Getter
	private static ConfigHandler configHandler;

	@Getter
	private static SetupService setupService;

	@Override
	public void onEnable() {
		plugin = this;
		pluginManager = getServer().getPluginManager();

		prepareConfig();
		prepareStorage();

		registerEvents();
		registerCommands();

		if (!checkStability()) {
			Bukkit.getLogger().info(ErrorHandler.PLUGIN_PREFIX + "Stabilität nicht gewährleistet. Das Plugin wird deaktiviert");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}

		setupService = new SetupService();

		plugin.getServer().getLogger().info(ErrorHandler.PLUGIN_PREFIX + ChatColor.GREEN + "Plugin erfolgreich geladen");
	}

	private boolean checkStability() {
		if(!MySQL.Setup.getInstance().isEveryConnectionConnected()) {
			return false;
		}

		return true;
	}

	@Override
	public void onDisable() {
		MySQL.Setup.getInstance().cleanupConnections();
	}

	private void prepareConfig() {
		configHandler = new ConfigHandler();
		MySQL.Setup.initialize(
			List.of(MySQLHandler.class)
		);
	}

	private void prepareStorage() {
		MySQLHandler.getInstance().createConfigurationValuesTable();
		MySQLHandler.getInstance().createLocationsTable();
		MySQLHandler.getInstance().createStatsTable();
	}

	private void registerEvents() {
		pluginManager.registerEvents(new JoinListener(), plugin);
	}

	private void registerCommands() {
		getCommand("stats").setExecutor(new StatsCommand());
	}
}