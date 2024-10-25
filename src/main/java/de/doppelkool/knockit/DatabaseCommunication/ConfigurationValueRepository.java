package de.doppelkool.knockit.DatabaseCommunication;

import de.doppelkool.knockit.errorhandling.ErrorHandler;
import lombok.Getter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Class Description
 *
 * @author doppelkool | github.com/doppelkool
 */
public class ConfigurationValueRepository extends Repository {

	@Getter
	private static ConfigurationValueRepository instance;

	@Getter
	private final ConfigurationValues configValues;

	public ConfigurationValueRepository() {
		instance = this;

		configValues = new ConfigurationValues();

		Optional<Object> optDeathHeight = findByName(DBConfigValueNames.deathHeight);
		optDeathHeight.ifPresent(
			o -> configValues.setDeathHeight((Integer) o));

		Optional<Object> optsetupFinished = findByName(DBConfigValueNames.setupFinished);
		optsetupFinished.ifPresent(
			o -> configValues.setSetupFinished((boolean) o));
	}

	public void update(DBConfigValueNames configName, Object value) {
		String query = "UPDATE configurationvalues SET " + configName + " = ? ";
		PreparedStatement preparedStatement = null;

		try {
			preparedStatement = getConnection().prepareStatement(query);
			preparedStatement.setObject(1, value);
			preparedStatement.executeUpdate();
		} catch (SQLException ex) {
			ErrorHandler.handleSQLError(ex);
		} finally {
			try {
				if (preparedStatement != null) preparedStatement.close();
			} catch (SQLException ex) {
				ErrorHandler.handleSQLError(ex);
			}
		}
		updateLocal(configName);
	}

	public void updateLocal(DBConfigValueNames configName) {
		Optional<Object> configValue = findByName(configName);
		if(configName.equals(DBConfigValueNames.deathHeight)) {
			configValues.setDeathHeight((Integer) configValue.get());
		}
		if(configName.equals(DBConfigValueNames.setupFinished)) {
			configValues.setSetupFinished((boolean) configValue.get());
		}
	}

	public Optional<Object> findByName(DBConfigValueNames configName) {
		String query = "SELECT " + configName + " FROM configurationvalues";
		ResultSet resultSet = null;

		try {
			resultSet = getConnection()
						.prepareStatement(query)
						.executeQuery();
			if (resultSet.next()) {
				return Optional.of(resultSet.getObject(1));
			}
		} catch (SQLException ex) {
			ErrorHandler.handleSQLError(ex);
		} finally {
			try {
				if (resultSet != null) resultSet.close();
			} catch (SQLException ex) {
				ErrorHandler.handleSQLError(ex);
			}
		}
		return Optional.empty();
	}

	public enum DBConfigValueNames {
		deathHeight,
		setupFinished,

		;

		@Override
		public String toString() {
			return this.name();
		}
	}
}
