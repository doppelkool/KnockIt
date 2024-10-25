package de.doppelkool.knockit.service;

import de.doppelkool.knockit.ConfigHandling.KnockItLocation;
import de.doppelkool.knockit.DatabaseCommunication.ConfigurationValueRepository;
import de.doppelkool.knockit.DatabaseCommunication.LocationRepository;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

/**
 * Class Description
 *
 * @author doppelkool | github.com/doppelkool
 */
@Getter
public class SetupService {

	private static SetupService instance = null;

	@Setter
	private Player playerToSetup = null;

	public boolean isSetup() {
		KnockItLocation spawnpoint = LocationRepository.getInstance().getSpawnpoint();
		Integer deathHeight = ConfigurationValueRepository.getInstance().getConfigValues().getDeathHeight();

		return spawnpoint != null
			&& deathHeight != null;
	}

	public static SetupService getInstance() {
		if(instance == null) {
			instance = new SetupService();
		}
		return instance;
	}

}
