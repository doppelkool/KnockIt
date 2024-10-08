package de.doppelkool.knockit.listener;

import de.doppelkool.knockit.service.PlayerStats;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Class Description
 *
 * @author doppelkool | github.com/doppelkool
 */
public class JoinListener implements Listener {
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		PlayerStats.createDefaultStatsForPlayer(e.getPlayer().getUniqueId().toString());
	}
}
