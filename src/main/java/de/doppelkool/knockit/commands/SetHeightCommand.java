package de.doppelkool.knockit.commands;

import de.doppelkool.knockit.ConfigHandling.KnockItLocation;
import de.doppelkool.knockit.DatabaseCommunication.ConfigurationValueRepository;
import de.doppelkool.knockit.DatabaseCommunication.ConfigurationValues;
import de.doppelkool.knockit.DatabaseCommunication.LocationRepository;
import de.doppelkool.knockit.service.SetupService;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

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

		if (ConfigurationValueRepository.getInstance().getConfigValues().isSetupFinished()) {
			sender.sendMessage("Dieser Befehl ist nicht bekannt");
			return true;
		}

		int deathHeight = pl.getLocation().getBlockY();

		KnockItLocation spawnpoint = LocationRepository.getInstance().getSpawnpoint();
		if (spawnpoint != null && spawnpoint.getY() <= deathHeight) {
			pl.sendMessage("Die DeathHeight darf nicht auf oder überhalb des Spawnpoints liegen");
			return true;
		}

		ConfigurationValueRepository
			.getInstance()
			.update(ConfigurationValueRepository.DBConfigValueNames.deathHeight, deathHeight);
		pl.sendMessage("Die DeathHeight wurde erfolgreich geändert");

		return true;
	}
}
