package de.doppelkool.knockit.commands;

import de.doppelkool.knockit.ConfigHandling.KnockItLocation;
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
public class SetHeightCommand implements CommandExecutor {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
		if (!(sender instanceof Player pl)) {
			sender.sendMessage("Diesen Befehl kannst du nicht als Console ausführen");
			return true;
		}

		int deathHeight = pl.getLocation().getBlockY();

		KnockItLocation spawnpoint = LocationRepository.getInstance().getSpawnpoint();
		if (spawnpoint != null && spawnpoint.getY() <= deathHeight) {
			pl.sendMessage("Die DeathHeight darf nicht auf oder überhalb des Spawnpoints liegen");
			return true;
		}

		boolean alreadyExist = ConfigurationValueRepository.getInstance().getConfigValues().getDeathHeight() != null;
		if (alreadyExist) {
			ConfigurationValueRepository
				.getInstance()
				.update(ConfigurationValueRepository.DBConfigValueNames.deathHeight, deathHeight);
			pl.sendMessage("Die DeathHeight wurde erfolgreich geändert");
		} else {
			ConfigurationValueRepository
				.getInstance()
				.save(ConfigurationValueRepository.DBConfigValueNames.deathHeight, deathHeight);
			pl.sendMessage("Die DeathHeight wurde erfolgreich gesetzt");
		}

		return true;
	}
}
