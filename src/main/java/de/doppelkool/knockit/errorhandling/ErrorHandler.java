package de.doppelkool.knockit.errorhandling;

import de.doppelkool.knockit.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.sql.SQLException;

/**
 * Class Description
 *
 * @author doppelkool | github.com/doppelkool
 */
public class ErrorHandler {

	public static final String PLUGIN_PREFIX = ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + "KNOCKIT" + ChatColor.DARK_GRAY + "]: " + ChatColor.RED;


	public static void handleSQLError(SQLException ex) {
		Main.getPlugin().getLogger().info(PLUGIN_PREFIX + "Es ist ein SQL-Fehler aufgetreten");
		ex.printStackTrace();
	}
	public static void handleStatsErrorToConsole(String playerName) {
		Main.getPlugin().getLogger().info(PLUGIN_PREFIX + "Es ist ein Fehler beim Abrufen der Stats vom Spieler " + playerName + " aufgetreten");
	}
	public static void handleStatsErrorToPlayer(Player pl) {
		pl.sendMessage(PLUGIN_PREFIX + "Es ist ein Fehler beim Abrufen deiner Stats aufgetreten, melde dies bitte einem Administrator");
	}
}
