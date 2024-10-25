package de.doppelkool.knockit.commands;

import de.doppelkool.knockit.DatabaseCommunication.ConfigurationValueRepository;
import de.doppelkool.knockit.DatabaseCommunication.LocationRepository;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Class Description
 *
 * @author doppelkool | github.com/doppelkool
 */
public class SetSpawnCommand implements CommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
		if(!(sender instanceof Player pl)) {
			sender.sendMessage("Diesen Befehl kannst du nicht als Console ausführen");
			return true;
		}

		Location spawnpointLocation = pl.getLocation();

		Integer deathHeight = ConfigurationValueRepository.getInstance().getConfigValues().getDeathHeight();
		if(deathHeight != null && spawnpointLocation.getY() <= deathHeight) {
			pl.sendMessage("Der Spawnpoint darf nicht auf oder unterhalb der DeathHeight liegen");
			return true;
		}

		boolean alreadyExist = LocationRepository.getInstance().getSpawnpoint() != null;
		if(alreadyExist) {
			LocationRepository.getInstance().update(
				LocationRepository.DBLocationNames.SPAWNPOINT,
				spawnpointLocation);
			pl.sendMessage("Der Spawnpoint wurde erfolgreich geändert");
		} else {
			LocationRepository.getInstance().save(
				LocationRepository.DBLocationNames.SPAWNPOINT,
				spawnpointLocation);
			pl.sendMessage("Der Spawnpoint wurde erfolgreich gesetzt");
		}

		return true;
	}
}
