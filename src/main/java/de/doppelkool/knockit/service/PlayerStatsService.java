package de.doppelkool.knockit.service;

import de.doppelkool.knockit.DatabaseCommunication.PlayerStats;
import de.doppelkool.knockit.DatabaseCommunication.PlayerStatsRepository;
import lombok.Getter;

import java.util.Optional;

/**
 * Class Description
 *
 * @author doppelkool | github.com/doppelkool
 */
public class PlayerStatsService {

	private static PlayerStatsService instance = null;

	public void createDefaultStatsForNewPlayer(String playerUUID) {
		PlayerStatsRepository statRepo = PlayerStatsRepository.getInstance();
		if (!statRepo.existsByUUID(playerUUID)) {
			statRepo.save(PlayerStats.defaultValues(playerUUID));
		}
	}

	public Optional<PlayerStats> loadStats(String playerUUID) {
		return PlayerStatsRepository.getInstance().findByUUID(playerUUID);
	}

	public static PlayerStatsService getInstance() {
		if(instance == null) {
			instance = new PlayerStatsService();
		}
		return instance;
	}

}
