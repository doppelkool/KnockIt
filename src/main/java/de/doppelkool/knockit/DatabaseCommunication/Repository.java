package de.doppelkool.knockit.DatabaseCommunication;

import de.doppelkool.knockit.ConfigHandling.ConfigHandler;
import de.doppelkool.knockit.errorhandling.ErrorHandler;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;

/**
 * Class Description
 *
 * @author doppelkool | github.com/doppelkool
 */
public abstract class Repository {

	public static Connection getConnection() {
		String URL
			= "jdbc:mysql://" + ConfigHandler.MYSQL_HOST + ":" + ConfigHandler.MYSQL_PORT + "/" + ConfigHandler.MYSQL_DATABASE;

		try {
			Bukkit.getLogger().info("KnockIt - URL " + URL);
			Bukkit.getLogger().info("KnockIt - MYSQL_USERNAME " + ConfigHandler.MYSQL_USERNAME);
			Bukkit.getLogger().info("KnockIt - MYSQL_PASSWORD " + ConfigHandler.MYSQL_USERNAME);
			return DriverManager.getConnection(URL, ConfigHandler.MYSQL_USERNAME, ConfigHandler.MYSQL_PASSWORD);
		} catch (SQLException ex) {
			ErrorHandler.handleSQLError(ex);
		}
		return null;
	}

}
