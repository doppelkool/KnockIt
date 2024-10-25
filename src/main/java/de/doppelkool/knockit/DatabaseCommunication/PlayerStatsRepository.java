package de.doppelkool.knockit.DatabaseCommunication;

import de.doppelkool.knockit.errorhandling.ErrorHandler;
import lombok.Getter;

import java.sql.*;
import java.util.Optional;

/**
 * Class Description
 *
 * @author doppelkool | github.com/doppelkool
 */
public class PlayerStatsRepository extends Repository {

	@Getter
	private static PlayerStatsRepository instance;

	public PlayerStatsRepository() {
		instance = this;
	}

	public boolean existsByUUID(String playerUUID) {
		String query = "SELECT EXISTS (SELECT kills,deaths,coins,tokens FROM stats WHERE playerUUID = ?)";
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			preparedStatement = getConnection().prepareStatement(query);
			preparedStatement.setString(1, playerUUID);

			resultSet = preparedStatement.executeQuery();
			return resultSet.next() && resultSet.getBoolean(1);
		} catch (SQLException ex) {
			ErrorHandler.handleSQLError(ex);
		} finally {
			try {
				if (resultSet != null) resultSet.close();
				if (preparedStatement != null) preparedStatement.close();
			} catch (SQLException ex) {
				ErrorHandler.handleSQLError(ex);
			}
		}
		return false;
	}

	public void save(PlayerStats playerStats) {
		String query = "INSERT INTO stats VALUES (?,?,?,?,?)";
		PreparedStatement preparedStatement = null;

		try {
			preparedStatement = getConnection().prepareStatement(query);
			preparedStatement.setString(1, playerStats.getPlayerUUID());
			preparedStatement.setInt(2, playerStats.getKills());
			preparedStatement.setInt(3, playerStats.getDeaths());
			preparedStatement.setInt(4, playerStats.getCoins());
			preparedStatement.setInt(5, playerStats.getTokens());
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

	public void update(PlayerStats playerStats) {
		String query = "UPDATE stats SET " +
			"kills = ?, " +
			"deaths = ?, " +
			"coins = ?, " +
			"tokens = ? " +
			"WHERE playerUUID = ?";
		PreparedStatement preparedStatement = null;

		try {
			preparedStatement = getConnection().prepareStatement(query);
			preparedStatement.setInt(1, playerStats.getKills());
			preparedStatement.setInt(2, playerStats.getDeaths());
			preparedStatement.setInt(3, playerStats.getCoins());
			preparedStatement.setInt(4, playerStats.getTokens());
			preparedStatement.setString(5, playerStats.getPlayerUUID());
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

	public Optional<PlayerStats> findByUUID(String playerUUID) {
		String query = "SELECT kills,deaths,coins,tokens FROM stats WHERE playerUUID = ?";
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			preparedStatement = getConnection().prepareStatement(query);
			preparedStatement.setString(1, playerUUID);

			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				int kills = resultSet.getInt("kills");
				int deaths = resultSet.getInt("deaths");
				int coins = resultSet.getInt("coins");
				int tokens = resultSet.getInt("tokens");

				return Optional.of(new PlayerStats(
					playerUUID,
					kills,
					deaths,
					coins,
					tokens));
			}
		} catch (SQLException ex) {
			ErrorHandler.handleSQLError(ex);
		} finally {
			try {
				if (resultSet != null) resultSet.close();
				if (preparedStatement != null) preparedStatement.close();
			} catch (SQLException ex) {
				ErrorHandler.handleSQLError(ex);
			}
		}
		return Optional.empty();
	}
}
