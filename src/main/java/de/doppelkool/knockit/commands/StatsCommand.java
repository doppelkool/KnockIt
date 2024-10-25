package de.doppelkool.knockit.commands;

import de.doppelkool.knockit.DatabaseCommunication.ConfigurationValueRepository;
import de.doppelkool.knockit.errorhandling.ErrorHandler;
import de.doppelkool.knockit.DatabaseCommunication.PlayerStats;
import de.doppelkool.knockit.service.PlayerStatsService;
import de.doppelkool.knockit.service.SetupService;
import org.bukkit.ChatColor;
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
public class StatsCommand implements CommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
		if(!(sender instanceof Player pl)) {
			sender.sendMessage("Diesen Befehl kannst du nicht als Console ausführen");
			return true;
		}

		if (!ConfigurationValueRepository.getInstance().getConfigValues().isSetupFinished()) {
			sender.sendMessage("Dieser Befehl ist nicht bekannt");
			return true;
		}

		Optional<PlayerStats> statsOpt = PlayerStatsService.getInstance().loadStats(pl.getUniqueId().toString());

		if(statsOpt.isEmpty()) {
			ErrorHandler.handleStatsErrorToConsole(pl.getName());
			ErrorHandler.handleStatsErrorToPlayer(pl);
			return true;
		}

		String statsString = formatStatString(statsOpt.get());
		pl.sendMessage(statsString);
		return true;
	}

	private String formatStatString(PlayerStats stats) {
		String statString = "";

		statString += ChatColor.GRAY + "-------------------";
		statString += ChatColor.YELLOW + "Kills " + ChatColor.DARK_GRAY + ": " + ChatColor.GRAY + stats.getKills() + "\n";
		statString += ChatColor.YELLOW + "Deaths " + ChatColor.DARK_GRAY + ": " + ChatColor.GRAY + stats.getDeaths() + "\n";
		statString += ChatColor.YELLOW + "KD/R " + ChatColor.DARK_GRAY + ": " + ChatColor.GRAY + formatKD(stats.getKills(), stats.getDeaths());
		//statString += ChatColor.YELLOW + "Rank" + ChatColor.DARK_GRAY + ": " + ChatColor.GRAY + stats.getRank();
		statString += ChatColor.GRAY + "-------------------";

		return statString;
	}

	private String formatKD(int kills, int deaths) {
		if(kills == 0) return "0";
		if(deaths == 0) return String.valueOf(kills);

		return String.valueOf(
			Math.floor((double)kills / (double)deaths));
	}
}
