package de.doppelkool.knockit.service;

import de.doppelkool.knockit.db.ConfigurationValues;
import de.doppelkool.knockit.db.Locations;
import de.doppelkool.knockit.storage.MySQLHandler;
import lombok.Getter;

/**
 * Class Description
 *
 * @author doppelkool | github.com/doppelkool
 */
public class SetupService {

	@Getter
	private boolean isSetup;

	public SetupService() {
		isSetup = false;
		Locations locations = MySQLHandler.getInstance().getSetupLocations();
		ConfigurationValues configValues = MySQLHandler.getInstance().getSetupValues();

		isSetup =
			locations != null &&
			locations.getSpawnPoint() != null &&
			configValues.getDeathHeight() != null;
	}
}
