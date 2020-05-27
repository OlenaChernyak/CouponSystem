package Connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import common.SystemMalfunctionException;

public class ConnectionPool {

	private static ConnectionPool cp = null;
	private static final int MAX_CONNECTIONS = 10;
	private BlockingQueue<Connection> connections;

	private static Connection createConnection() throws SQLException {
		return DriverManager.getConnection("jdbc:mysql://localhost:3306/project?serverTimezone=UTC", "root", "0408");
	}

	public ConnectionPool() throws SystemMalfunctionException {
		connections = new LinkedBlockingQueue<>(MAX_CONNECTIONS);
		int remainingCapacity = connections.remainingCapacity();
		for (int i = 0; i < remainingCapacity; ++i) {
			try {
				connections.offer(createConnection());
			} catch (SQLException e) {
				throw new SystemMalfunctionException("FATAL: Can not establish connection to data base.");
			}
		}
	}

	public synchronized Connection getConnection() throws SystemMalfunctionException {
		try {
			return connections.take();
		} catch (InterruptedException e) {
			throw new SystemMalfunctionException("FATAL: Interrupted while trying to get an connection.");
		}
	}

	public static ConnectionPool getInstance() throws SystemMalfunctionException {
		if (cp == null) {
			// This line was return new ConnectionPool();
			// it should instead create an instance, in order for this class to be a
			// Singleton.
			cp = new ConnectionPool();
		}
		return cp;
	}

	public synchronized void returnConnection(Connection connection) throws SystemMalfunctionException {
		try {
			connections.put(connection);
		} catch (InterruptedException e) {
			throw new SystemMalfunctionException("Can not connect to server");
		}
	}

	public synchronized void closeConnections() throws SystemMalfunctionException {

		if (connections.remainingCapacity() == 0) {
			Connection c;
			while ((c = connections.poll()) != null) {
				try {
					c.close();
				} catch (SQLException e) {
					throw new SystemMalfunctionException("Error. Try later.");
				}
			}
		} else {
			throw new SystemMalfunctionException("Didn't close all connections");
		}

	}
}
