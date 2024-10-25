package de.doppelkool.knockit.listener;

import de.doppelkool.knockit.DatabaseCommunication.ConfigurationValueRepository;
import de.doppelkool.knockit.DatabaseCommunication.LocationRepository;
import de.doppelkool.knockit.service.InventoryService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Class Description
 *
 * @author doppelkool | github.com/doppelkool
 */
public class OnMoveListener implements Listener {

	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		Player pl = e.getPlayer();
		if (pl.getLocation().getY() <= ConfigurationValueRepository.getInstance().getConfigValues().getDeathHeight()) {
			InventoryService.getInstance().loadDefaultInventory(pl);
			pl.teleport(LocationRepository.getInstance().getSpawnpoint());
		}
	}
}
