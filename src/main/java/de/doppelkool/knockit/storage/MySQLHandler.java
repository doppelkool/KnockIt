package de.doppelkool.knockit.storage;

import de.doppelkool.knockit.Main;
import de.doppelkool.knockit.db.ConfigurationValues;
import de.doppelkool.knockit.db.Locations;
import de.doppelkool.knockit.errorhandling.ErrorHandler;
import de.doppelkool.knockit.service.PlayerStats;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Class Description
 *
 * @author doppelkool | github.com/doppelkool
 */
public class MySQLHandler extends MySQL {

	private static MySQLHandler instance;

	public static MySQLHandler getInstance() {
		if (instance == null)
			instance = new MySQLHandler();
		return instance;
	}

	public MySQLHandler() {
		super("knockit",
			Main.getConfigHandler().getMYSQL_DATABASE(),
			Main.getConfigHandler().getMYSQL_HOST(),
			Main.getConfigHandler().getMYSQL_PORT(),
			Main.getConfigHandler().getMYSQL_USERNAME(),
			Main.getConfigHandler().getMYSQL_PASSWORD());
		instance = this;
	}
	
	public void createConfigurationValuesTable() {
		String createTableSQL = "CREATE TABLE IF NOT EXISTS `configurationvalues` " +
			"(" +
			"    `deathHeight` int(11) NOT NULL" +
			")";
		String insertDefaultValue = "INSERT INTO `configurationvalues` (`deathHeight`) " +
			"SELECT -64 " +
			"WHERE NOT EXISTS (" +
			"    SELECT 1 FROM `configurationvalues` WHERE `deathHeight` = -64" +
			");";
		this.executeUpdateSQL(createTableSQL);
		this.executeUpdateSQL(insertDefaultValue);
	}

	public void createLocationsTable() {
		String createTableSQL = "CREATE TABLE IF NOT EXISTS `locations`" +
			"(" +
			"  `locationName` varchar(200) NOT NULL," +
			"  `worldName` varchar(200) NOT NULL," +
			"  `X` double NOT NULL," +
			"  `Y` double NOT NULL," +
			"  `Z` double NOT NULL," +
			"  `yaw` float NOT NULL," +
			"  `pitch` float NOT NULL" +
			")";
		this.executeUpdateSQL(createTableSQL);
	}

	public void createStatsTable() {
		String createTableSQL = "CREATE TABLE IF NOT EXISTS `stats` " +
			"(" +
			"  `playerUUID` varchar(36) NOT NULL," +
			"  `kills` int(11) NOT NULL DEFAULT 0," +
			"  `deaths` int(11) NOT NULL DEFAULT 0," +
			"  `coins` int(11) NOT NULL DEFAULT 0," +
			"  `tokens` int(11) NOT NULL DEFAULT 100," +
			"  PRIMARY KEY (`playerUUID`)" +
			")";
		this.executeUpdateSQL(createTableSQL);
	}

	public Locations getSetupLocations() {
		List<Locations> locations = instance.getObjectsFromDBbyParams("*", "locations", Map.of(), Locations.class);
		return locations.isEmpty() ? null: locations.get(0);
	}

	public ConfigurationValues getSetupValues() {
		Integer deathHeight = instance.getObjectFromDBbyParams("deathHeight", "configurationvalues", Map.of(), Integer.class);
		return new ConfigurationValues(deathHeight);
	}

	public void createDefaultStatsForPlayer(String playerUUID) {
		String createDefaultStatsSQL = "INSERT IGNORE INTO stats " +
			"(playerUUID) " +
			"VALUES ('" + playerUUID + "')";
		this.executeUpdateSQL(createDefaultStatsSQL);
	}

	public Optional<PlayerStats> getPlayerStats(String uuid) {
		PlayerStats objectsFromDBbyParams = instance.getObjectFromDBbyParams("playerUUID,kills,deaths,coins,tokens", "stats", Map.of("playerUUID", uuid), PlayerStats.class);
		return Optional.ofNullable(objectsFromDBbyParams);
	}
}
