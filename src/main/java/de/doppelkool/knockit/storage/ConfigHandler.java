package de.doppelkool.knockit.storage;

import lombok.Getter;

/**
 * Class Description
 *
 * @author doppelkool | github.com/doppelkool
 */
@Getter
public class ConfigHandler extends YMLFileHandler {

	private String MYSQL_HOST;
	private String MYSQL_PORT;
	private String MYSQL_USERNAME;
	private String MYSQL_PASSWORD;
	private String MYSQL_DATABASE;


	public ConfigHandler() {
		super("config");

		MYSQL_HOST = yamlConfig.getString("mysql.host");
		MYSQL_PORT = yamlConfig.getString("mysql.port");
		MYSQL_USERNAME = yamlConfig.getString("mysql.username");
		MYSQL_PASSWORD = yamlConfig.getString("mysql.password");
		MYSQL_DATABASE = yamlConfig.getString("mysql.database");
	}
}
