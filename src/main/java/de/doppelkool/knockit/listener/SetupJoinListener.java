package de.doppelkool.knockit.listener;

import de.doppelkool.knockit.service.SetupService;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Class Description
 *
 * @author doppelkool | github.com/doppelkool
 */
public class SetupJoinListener implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player pl = e.getPlayer();
		Player playerToSetup = SetupService.getInstance().getPlayerToSetup();

		//if (!pl.hasPermission("knockit.setup")) {
		//	pl.kickPlayer("You are not allowed to setup the plugin");
		//} else if (playerToSetup != null
		//		&& !playerToSetup.getName().equals(pl.getName())) {
		//	pl.kickPlayer("There is another player setting up the plugin");
		//}
		SetupService.getInstance().setPlayerToSetup(pl);

		String setupMessage = "/spawnpoint - To set the spawnpoint; /setheight - To set the height on which the people die; /finish - To finish the setup";
		pl.sendMessage(setupMessage);
	}
}
