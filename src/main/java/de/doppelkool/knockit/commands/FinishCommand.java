package de.doppelkool.knockit.commands;

import de.doppelkool.knockit.ConfigHandling.KnockItLocation;
import de.doppelkool.knockit.DatabaseCommunication.ConfigurationValueRepository;
import de.doppelkool.knockit.DatabaseCommunication.LocationRepository;
import de.doppelkool.knockit.service.SetupService;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
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
public class FinishCommand implements CommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
		if(!(sender instanceof Player pl)) {
			sender.sendMessage("Diesen Befehl kannst du nicht als Console ausführen");
			return true;
		}

		if (ConfigurationValueRepository.getInstance().getConfigValues().isSetupFinished()) {
			sender.sendMessage("Dieser Befehl ist nicht bekannt");
			return true;
		}

		Player playerToSetup = SetupService.getInstance().getPlayerToSetup();

		if(!playerToSetup.getUniqueId().toString().equals(pl.getUniqueId().toString())) {
			pl.sendMessage("Diese Nachricht sollte nicht möglich sein, da sich kein Spieler auf dem Server befinden dürfte außer derjenige der es aufsetzt");
			return true;
		}

		if (args.length == 1
				&& args[0].equalsIgnoreCase("yes")) {
			pl.kickPlayer("Setup erfolgreich!");
			SetupService.getInstance().finishSetupAndStartGame();
			return true;
		}

		KnockItLocation spawnpoint = LocationRepository.getInstance().getSpawnpoint();
		Integer deathHeight = ConfigurationValueRepository.getInstance().getConfigValues().getDeathHeight();

		pl.sendMessage("Bist du dir sicher dass du das Setup mit den folgenden Werten abschließen möchtest?");
		pl.sendMessage("- Spawnpoint: " + KnockItLocation.serialize(spawnpoint));
		pl.sendMessage("- DeathHeight: " + deathHeight);

		TextComponent yesButton = new TextComponent("[Yes]");
		yesButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/finish yes"));
		yesButton.setBold(true);
		yesButton.setColor(ChatColor.GREEN);

		pl.spigot().sendMessage(yesButton);

		return true;
	}
}
