package de.doppelkool.knockit.DatabaseCommunication;

import de.doppelkool.knockit.ConfigHandling.KnockItLocation;
import de.doppelkool.knockit.errorhandling.ErrorHandler;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Class Description
 *
 * @author doppelkool | github.com/doppelkool
 */
public class LocationRepository extends Repository {

	@Getter
	private static LocationRepository instance;

	@Getter
	private KnockItLocation spawnpoint;

	public LocationRepository() {
		instance = this;
		spawnpoint = findByName(DBLocationNames.SPAWNPOINT).orElse(null);
	}

	public void save(DBLocationNames locationName, Location loc) {
		updateLocal(locationName, loc);

		String query = "INSERT INTO locations VALUES (?,?,?,?,?,?,?)";
		PreparedStatement preparedStatement = null;

		try {
			preparedStatement = getConnection().prepareStatement(query);
			preparedStatement.setString(1, locationName.toString());
			preparedStatement.setString(2, loc.getWorld().getName());
			preparedStatement.setDouble(3, loc.getX());
			preparedStatement.setDouble(4, loc.getY());
			preparedStatement.setDouble(5, loc.getZ());
			preparedStatement.setFloat(6, loc.getYaw());
			preparedStatement.setFloat(7, loc.getPitch());
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

	public void update(DBLocationNames locationName, Location loc) {
		updateLocal(locationName, loc);

		String query = "UPDATE locations SET " +
			"worldName = ?, " +
			"X = ?, " +
			"Y = ?, " +
			"Z = ?, " +
			"yaw = ?, " +
			"pitch = ? " +
			"WHERE locationName = ?";
		PreparedStatement preparedStatement = null;

		try {
			preparedStatement = getConnection().prepareStatement(query);
			preparedStatement.setString(1, locationName.toString());
			preparedStatement.setString(2, loc.getWorld().getName());
			preparedStatement.setDouble(3, loc.getX());
			preparedStatement.setDouble(4, loc.getY());
			preparedStatement.setDouble(5, loc.getZ());
			preparedStatement.setFloat(6, loc.getYaw());
			preparedStatement.setFloat(7, loc.getPitch());
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

	private void updateLocal(DBLocationNames locationName, Location loc) {
		if(locationName.equals(DBLocationNames.SPAWNPOINT)) {
			spawnpoint = new KnockItLocation(loc);
		}
	}

	public Optional<KnockItLocation> findByName(DBLocationNames locationName) {
		String query = "SELECT worldName, X, Y, Z, yaw, pitch FROM locations WHERE locationName = ?";
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			preparedStatement = getConnection().prepareStatement(query);
			preparedStatement.setString(1, locationName.toString());

			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {

				return Optional.of(new KnockItLocation(
				 resultSet.getString("worldName"),
					resultSet.getDouble("X"),
					resultSet.getDouble("Y"),
					resultSet.getDouble("Z"),
					resultSet.getFloat("yaw"),
					resultSet.getFloat("pitch")
				));
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

	public enum DBLocationNames {
		SPAWNPOINT;

		@Override
		public String toString() {
			return this.name();
		}
	}
}
