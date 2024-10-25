package de.doppelkool.knockit.service;

import de.doppelkool.knockit.ConfigHandling.KnockItLocation;
import de.doppelkool.knockit.DatabaseCommunication.ConfigurationValueRepository;
import de.doppelkool.knockit.DatabaseCommunication.LocationRepository;
import de.doppelkool.knockit.Main;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

/**
 * Class Description
 *
 * @author doppelkool | github.com/doppelkool
 */
public class SetupService {

	private static SetupService instance = null;

	@Getter
	@Setter
	private Player playerToSetup = null;

	public boolean isSetup() {
		LocationRepository.getInstance().updateLocal(LocationRepository.DBLocationNames.SPAWNPOINT);
		ConfigurationValueRepository.getInstance().updateLocal(ConfigurationValueRepository.DBConfigValueNames.deathHeight);
		ConfigurationValueRepository.getInstance().updateLocal(ConfigurationValueRepository.DBConfigValueNames.setupFinished);

		KnockItLocation spawnpoint = LocationRepository.getInstance().getSpawnpoint();
		Integer deathHeight = ConfigurationValueRepository.getInstance().getConfigValues().getDeathHeight();
		boolean setupFinished = ConfigurationValueRepository.getInstance().getConfigValues().isSetupFinished();

		return spawnpoint != null
			&& deathHeight != null
			&& setupFinished;
	}

	public void finishSetupAndStartGame() {
		ConfigurationValueRepository.getInstance().update(ConfigurationValueRepository.DBConfigValueNames.setupFinished, true);

		HandlerList.unregisterAll(Main.SetupListeners.setupJoinListener);
		Bukkit.getPluginManager().registerEvents(Main.GameListeners.gameJoinListener, Main.getPlugin());
		Bukkit.getPluginManager().registerEvents(Main.GameListeners.fallDamageListener, Main.getPlugin());
	}

	public static SetupService getInstance() {
		if(instance == null) {
			instance = new SetupService();
		}
		return instance;
	}

}
