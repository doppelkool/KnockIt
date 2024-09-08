package de.doppelkool.knockit.commands;

import de.doppelkool.knockit.errorhandling.ErrorHandler;
import de.doppelkool.knockit.service.PlayerStats;
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

		Optional<PlayerStats> statsOpt = PlayerStats.loadStats(pl.getUniqueId().toString());

		if(statsOpt.isEmpty()) {
			ErrorHandler.handleErrorToConsole();
			ErrorHandler.handleErrorToPlayer();
			return true;
		}

		String statsString = formatStatString(statsOpt.get());
		pl.sendMessage(statsString);
		return true;
	}

	private String formatStatString(PlayerStats stats) {
		String statString = "";

		statString += ChatColor.GRAY + "-------------------";
		statString += ChatColor.YELLOW + "Kills" + ChatColor.DARK_GRAY + ": " + ChatColor.GRAY + stats.getKills();
		statString += ChatColor.YELLOW + "Deaths" + ChatColor.DARK_GRAY + ": " + ChatColor.GRAY + stats.getDeaths();
		statString += ChatColor.YELLOW + "KD/R" + ChatColor.DARK_GRAY + ": " + ChatColor.GRAY + formatKD(stats.getKills(), stats.getDeaths());
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
