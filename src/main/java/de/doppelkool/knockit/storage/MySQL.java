package de.doppelkool.knockit.storage;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import de.doppelkool.knockit.errorhandling.ErrorHandler;
import org.bukkit.Bukkit;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class Description
 *
 * @author doppelkool | github.com/doppelkool
 */
public abstract class MySQL {
	protected HikariDataSource ds;

	public boolean isConnected() {
		return this.isConnected;
	}

	private boolean isConnected = true;

	protected MySQL(String pluginName, String database, String host, String port, String user, String password) {
		String jdbcUrl = "jdbc:mysql://" + host + ":" + port + "/";
		createDatabaseIfNotExists(jdbcUrl, user, password, database);

		HikariConfig config = new HikariConfig();
		config.setPoolName(pluginName + "-Pool");
		config.setJdbcUrl(jdbcUrl + database);
		config.setUsername(user);
		config.setPassword(password);
		config.setMaximumPoolSize(5);
		config.setDriverClassName("com.mysql.cj.jdbc.Driver");
		config.setAutoCommit(false);
		config.setLeakDetectionThreshold(30000L);
		config.addDataSourceProperty("useJDBCCompliantTimezoneShift", Boolean.valueOf(true));
		config.addDataSourceProperty("useLegacyDatetimeCode", Boolean.valueOf(false));
		config.addDataSourceProperty("serverTimezone", "Europe/Berlin");
		this.ds = new HikariDataSource(config);

		Connection connection = null;
		try {
			connection = getConn();
			if (connection == null)
				throw new SQLException();
		} catch (SQLException e) {
			this.isConnected = false;
		} finally {
			cleanup(connection, null, null);
		}
	}

	private void createDatabaseIfNotExists(String jdbcUrl, String user, String password, String database) {
		try (Connection connection = DriverManager.getConnection(jdbcUrl, user, password);
		     PreparedStatement statement = connection.prepareStatement("CREATE DATABASE IF NOT EXISTS " + database)) {
			 statement.executeUpdate();
			 System.out.println("Database created or already exists!");
		} catch (SQLException e) {
			ErrorHandler.handleSQLError(e);;
		}
	}

	public Connection getConn() throws SQLException {
		return this.ds.getConnection();
	}

	public void cleanup() {
		if (this.ds != null)
			this.ds.close();
	}

	public void cleanup(Connection c, PreparedStatement pst, ResultSet rs) {
		if (c != null) {
			try {
				c.commit();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				c.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (pst != null) {
			try {
				pst.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	protected void executeUpdateSQL(String sqlString) {
		Connection c = null;
		PreparedStatement pst = null;
		try {
			c = ds.getConnection();
			pst = c.prepareStatement(sqlString);
			pst.executeUpdate();
		} catch (SQLException e) {
			ErrorHandler.handleSQLError(e);
		} finally {
			cleanup(c, pst, null);
		}
	}

	protected <T> T getObjectFromDBbyParams(String dbLabel, String tableName, Map<String, String> params, Class<T> clazz) {
		StringBuilder a = new StringBuilder("SELECT " + dbLabel + " FROM " + tableName + " ");
		insertWhereSpecifications(a, params);
		Connection c = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			c = this.ds.getConnection();
			pst = c.prepareStatement(a.toString());
			rs = pst.executeQuery();
			if (rs.next()) {
				Object objectFromDB = rs.getObject(dbLabel);
				return clazz.cast(objectFromDB);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			cleanup(c, pst, rs);
		}
		return null;
	}

	protected <T> List<T> getObjectsFromDBbyParams(String dbLabel, String tableName, Map<String, String> params, Class<T> clazz) {
		StringBuilder a = new StringBuilder("SELECT " + dbLabel + " FROM " + tableName + " ");
		insertWhereSpecifications(a, params);
		List<T> listOfResult = new ArrayList<>();
		Connection c = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			c = this.ds.getConnection();
			pst = c.prepareStatement(a.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				Object objectFromDB = rs.getObject(dbLabel);
				listOfResult.add(clazz.cast(objectFromDB));
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			cleanup(c, pst, rs);
		}
		return listOfResult;
	}

	private void insertWhereSpecifications(StringBuilder sqlString, Map<String, String> params) {
		if (params.entrySet().size() == 0)
			return;
		List<Map.Entry<String, String>> entries = params.entrySet().stream().toList();
		sqlString.append("WHERE ");
		for (int i = 0; i < entries.size(); i++) {
			Map.Entry<String, String> paramPair = entries.get(i);
			sqlString
				.append(paramPair.getKey())
				.append("=")
				.append(paramPair.getValue())
				.append(",");
			if (entries.indexOf(paramPair) != entries.size() - 1)
				sqlString.append(" AND ");
		}
	}

	public static class Setup {
		private static Setup INSTANCE;

		private static HashMap<Class<? extends MySQL>, Boolean> dbInstanceToConnected = new HashMap<>();

		public boolean isEveryConnectionConnected() {
			return this.everyConnectionConnected;
		}

		private final boolean everyConnectionConnected = !dbInstanceToConnected.containsValue(Boolean.valueOf(false));

		public static Setup initialize(List<Class<? extends MySQL>> dbClasses) {
			dbClasses.forEach(dbClass -> dbInstanceToConnected.put(dbClass, Boolean.valueOf(false)));
			return startInstances();
		}

		private static Setup startInstances() {
				for (Class<? extends MySQL> clazz : dbInstanceToConnected.keySet()) {
					MySQL instance = null;
					try {
						instance = (MySQL)clazz.getMethod("getInstance", new Class[0]).invoke(null, new Object[0]);
					} catch (IllegalAccessException e) {
						throw new RuntimeException(e);
					} catch (InvocationTargetException e) {
						throw new RuntimeException(e);
					} catch (NoSuchMethodException e) {
						throw new RuntimeException(e);
					}
					dbInstanceToConnected.put(clazz, Boolean.valueOf(instance.isConnected()));
				}
				return getInstance();
		}

		public static Setup getInstance() {
			if (INSTANCE == null)
				INSTANCE = new Setup();
			return INSTANCE;
		}

		public void cleanupConnections() {
			for (Class<? extends MySQL> clazz : dbInstanceToConnected.keySet()) {
				try {
					((MySQL)clazz.getMethod("getInstance", new Class[0]).invoke(null, new Object[0])).cleanup();
				} catch (IllegalAccessException e) {
					throw new RuntimeException(e);
				} catch (InvocationTargetException e) {
					throw new RuntimeException(e);
				} catch (NoSuchMethodException e) {
					throw new RuntimeException(e);
				}
			}
			dbInstanceToConnected.clear();
		}
	}
}
