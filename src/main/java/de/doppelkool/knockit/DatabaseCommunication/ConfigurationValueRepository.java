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
	}

	public void save(DBConfigValueNames configName, Object value) {
		updateLocal(configName, value);

		String query = "INSERT INTO configurationvalues VALUES (?)";
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
	}

	public void update(DBConfigValueNames configName, Object value) {
		updateLocal(configName, value);

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
	}

	private void updateLocal(DBConfigValueNames configName, Object value) {
		if(configName.equals(DBConfigValueNames.deathHeight)) {
			configValues.setDeathHeight((Integer) value);
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
				return Optional.of(resultSet.getInt(1));
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
		deathHeight;

		@Override
		public String toString() {
			return this.name();
		}
	}
}
