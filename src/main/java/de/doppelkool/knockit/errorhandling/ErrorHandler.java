package de.doppelkool.knockit.errorhandling;

import de.doppelkool.knockit.Main;
import org.bukkit.ChatColor;

import java.sql.SQLException;

/**
 * Class Description
 *
 * @author doppelkool | github.com/doppelkool
 */
public class ErrorHandler {

	public static final String PLUGIN_PREFIX = ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + "KNOCKIT" + ChatColor.DARK_GRAY + "]: " + ChatColor.RED;

	public static void handleErrorToPlayer() {
		Main.getPlugin().getLogger().info(PLUGIN_PREFIX + "Es ist ein Fehler aufgetreten, melde dies bitte einem Administrator");
	}
	public static void handleErrorToConsole() {
		Main.getPlugin().getLogger().info(PLUGIN_PREFIX + "Es ist ein Fehler aufgetreten");
	}

	public static void handleSQLError(SQLException ex) {
		Main.getPlugin().getLogger().info(PLUGIN_PREFIX + "Es ist ein SQL-Fehler aufgetreten");
		ex.printStackTrace();
	}
}
