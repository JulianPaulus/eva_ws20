package battleships.server.db;

import org.flywaydb.core.Flyway;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DBConnection {
	private static DBConnection instance;
	private Map<Long, Connection> threadConnections = new HashMap<>();

	public static synchronized DBConnection getInstance() {
		if(instance == null) {
			try {
				instance = new DBConnection();
			} catch (SQLException throwables) {
				throwables.printStackTrace();
				System.exit(1);
			}
		}
		return instance;
	}

	private DBConnection() throws SQLException {
		Flyway flyway = Flyway.configure().dataSource("jdbc:sqlite:server.db", null, null).load();
		flyway.migrate();
	}

	public synchronized Connection getConnection() throws SQLException {
		Connection connection = threadConnections.get(Thread.currentThread().getId());
		if(connection == null || connection.isClosed()) {
			connection = DriverManager.getConnection("jdbc:sqlite:server.db");
			connection.setAutoCommit(false);
			threadConnections.put(Thread.currentThread().getId(), connection);
		}
		return connection;
	}
}
