package de.doppelkool.knockit.listener;

import de.doppelkool.knockit.DatabaseCommunication.LocationRepository;
import de.doppelkool.knockit.service.InventoryService;
import de.doppelkool.knockit.service.PlayerStatsService;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Class Description
 *
 * @author doppelkool | github.com/doppelkool
 */
public class GameJoinListener implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player pl = e.getPlayer();

		PlayerStatsService.getInstance().createDefaultStatsForNewPlayer(pl.getUniqueId().toString());
		pl.setGameMode(GameMode.ADVENTURE);
		InventoryService.getInstance().loadDefaultInventory(pl);
		pl.teleport(LocationRepository.getInstance().getSpawnpoint());
	}

}
