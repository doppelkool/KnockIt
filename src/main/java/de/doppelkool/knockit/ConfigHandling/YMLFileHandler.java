package de.doppelkool.knockit.ConfigHandling;

import de.doppelkool.knockit.Main;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Class Description
 *
 * @author doppelkool | github.com/doppelkool
 */
public abstract class YMLFileHandler {

	public String resourceName;
	public YamlConfiguration yamlConfig;

	public YMLFileHandler(String resourceName) {
		this.resourceName = resourceName + ".yml";
		setup();
	}

	private void setup() {
		File dataFolder = Main.getPlugin().getDataFolder();
		if (!dataFolder.exists()) {
			dataFolder.mkdirs();
		}

		File file = new File(dataFolder, resourceName);
		if(!file.exists()) {
			try {
				Files.copy(
					getClass().getClassLoader().getResourceAsStream(resourceName),
					file.toPath());
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		if(!file.isFile()) {
			throw new RuntimeException("Location(" + file.getPath() + ") does point to a not-file");
		}

		loadFile();
	}

	private void loadFile() {
		String fullPath = Main.getPlugin().getDataFolder().getPath() + "//" + resourceName;
		yamlConfig = YamlConfiguration.loadConfiguration(new File(fullPath));
	}
}
