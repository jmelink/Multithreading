package com.solvd.multithreading;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConnectionPool extends Thread {
	private CopyOnWriteArrayList<Connection> connections = new CopyOnWriteArrayList<>();
	private final static Integer MAX_CONNECTIONS = 5;
	private final static ConnectionPool INSTANCE = new ConnectionPool();
	private static ExecutorService executor;
	private ConcurrentLinkedQueue<Connection> connectionsToAdd = new ConcurrentLinkedQueue<>();
	private ConcurrentLinkedQueue<Connection> connectionsToRelease = new ConcurrentLinkedQueue<>();
	
	private final static Logger LOGGER = LogManager.getLogger(ConnectionPool.class.getClass());
	
	private ConnectionPool() {
		setExecutor(Executors.newFixedThreadPool(getMaxConnections()));
	}
	
	public CopyOnWriteArrayList<Connection> getConnections() {
		return connections;
	}

	public void setConnections(CopyOnWriteArrayList<Connection> connections) {
		this.connections = connections;
	}
	
	public Integer getMaxConnections() {
		return MAX_CONNECTIONS;
	}
	
	public static ConnectionPool getConnectionPool() {
		return INSTANCE;
	}
	
	public static ExecutorService getExecutor() {
		return executor;
	}

	public static void setExecutor(ExecutorService executor) {
		ConnectionPool.executor = executor;
	}
	
	public ConcurrentLinkedQueue<Connection> getConnectionsToAdd() {
		return connectionsToAdd;
	}

	public void setConnectionsToAdd(ConcurrentLinkedQueue<Connection> connectionsToAdd) {
		this.connectionsToAdd = connectionsToAdd;
	}

	public ConcurrentLinkedQueue<Connection> getConnectionsToRelease() {
		return connectionsToRelease;
	}

	public void setConnectionsToRelease(ConcurrentLinkedQueue<Connection> connectionsToRelease) {
		this.connectionsToRelease = connectionsToRelease;
	}

	public Connection getConnection() {
		Connection newConnection = new Connection();
		getConnectionsToAdd().add(newConnection);
		return newConnection;
	}
	
	public void releaseConnection(Connection connectionToRelease) {
		getConnectionsToRelease().add(connectionToRelease);
	}

	@Override
	public void run() {
		while (!this.isInterrupted()) {
			if (!getConnectionsToRelease().isEmpty()) {
				for (Connection connectionToRelease: getConnectionsToRelease()) {
					if (getConnections().size() > 0) {
						Boolean matchingActiveConnectionFound = false;
						for (Connection connection: getConnections()) {
							if (connectionToRelease.equals(connection)) {
								LOGGER.info("Connection #" + connectionToRelease.getConnectionNumber() + " released from the connection pool.");
								getConnections().remove(connectionToRelease);
								getConnectionsToRelease().remove(connectionToRelease);
								connectionToRelease.stop();
								matchingActiveConnectionFound = true;
								break;
							}
						}
						if (!matchingActiveConnectionFound) {
							LOGGER.info("No active connections match the requested connection to release...");
							getConnectionsToRelease().remove(connectionToRelease);
						}
					} else {
						LOGGER.info("There are no active connections.");
					}
				}
				
			}
			if (!getConnectionsToAdd().isEmpty()) {
				for (Connection connectionToAdd: getConnectionsToAdd()) {
					if (getConnections().isEmpty()) {
						LOGGER.info("Connection #" + connectionToAdd.getConnectionNumber() + " added to the connection pool.");
						getConnections().add(connectionToAdd);
						getConnectionsToAdd().remove(connectionToAdd);
						executor.submit(connectionToAdd);
					} else if (getConnections().size() < getMaxConnections()) {
						Boolean connectionAlreadyExists = false;
						for (Connection connection: getConnections()) {
							if (connectionToAdd.equals(connection)) {
								LOGGER.info("Active connection already open for connection #" + connectionToAdd.getConnectionNumber() + "...");
								getConnectionsToAdd().remove(connectionToAdd);
								connectionAlreadyExists = true;
								break;
							}
						}
						if (!connectionAlreadyExists) {
							LOGGER.info("Connection #" + connectionToAdd.getConnectionNumber() + " added to the connection pool.");
							getConnections().add(connectionToAdd);
							getConnectionsToAdd().remove(connectionToAdd);
							executor.submit(connectionToAdd);
						}
					}
				}
			}
		}
	}
}
