package de.doppelkool.knockit.ConfigHandling;

import lombok.Getter;
import lombok.Setter;

/**
 * Class Description
 *
 * @author doppelkool | github.com/doppelkool
 */
public class ConfigHandler extends YMLFileHandler {

	public static String MYSQL_HOST;
	public static String MYSQL_PORT;
	public static String MYSQL_USERNAME;
	public static String MYSQL_PASSWORD;
	public static String MYSQL_DATABASE;

	public ConfigHandler() {
		super("config");

		MYSQL_HOST = yamlConfig.getString("mysql.host");
		MYSQL_PORT = yamlConfig.getString("mysql.port");
		MYSQL_USERNAME = yamlConfig.getString("mysql.username");
		MYSQL_PASSWORD = yamlConfig.getString("mysql.password");
		MYSQL_DATABASE = yamlConfig.getString("mysql.database");
	}
}
