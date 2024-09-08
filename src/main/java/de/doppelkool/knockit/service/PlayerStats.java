package de.doppelkool.knockit.service;

import de.doppelkool.knockit.storage.MySQLHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

/**
 * Class Description
 *
 * @author doppelkool | github.com/doppelkool
 */
@Getter
@Setter
@AllArgsConstructor
public class PlayerStats {

	private String playerUUID;
	private int kills;
	private int deaths;
	private int coins;
	private int tokens;


	public static Optional<PlayerStats> loadStats(String playerUUID) {
		return MySQLHandler.getInstance().getPlayerStats(playerUUID);
	}

}
