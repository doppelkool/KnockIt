package de.doppelkool.knockit.DatabaseCommunication;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class Description
 *
 * @author doppelkool | github.com/doppelkool
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlayerStats {

	private String playerUUID;
	private int kills;
	private int deaths;
	private int coins;
	private int tokens;

	public static PlayerStats defaultValues(String playerUUID) {
		return new PlayerStats(playerUUID, 0, 0, 100, 0);
	}
}