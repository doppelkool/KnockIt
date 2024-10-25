package de.doppelkool.knockit.listener;

import de.doppelkool.knockit.DatabaseCommunication.LocationRepository;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

/**
 * Class Description
 *
 * @author doppelkool | github.com/doppelkool
 */
public class OnDeathListener implements Listener {

	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		Player pl = e.getEntity();
		e.setKeepInventory(true);
		e.setDeathMessage(null);
		pl.getInventory().clear();
	}

}
