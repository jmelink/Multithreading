package com.solvd.multithreading;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiFunction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConnectionManager {
	private final static Logger LOGGER = LogManager.getLogger(ConnectionManager.class.getClass());
	
	public static void main(String[] args) throws InterruptedException {
		ConnectionPool.getConnectionPool().start();
		
		LOGGER.info("--- Add Connections Up To Max Connections ---");
		for (int i = 0; i < ConnectionPool.getConnectionPool().getMaxConnections(); i++) {
			ConnectionPool.getConnectionPool().getConnection();
		}
		Thread.sleep(1000);
		
		LOGGER.info("--- Attempt To Add Connection Past Max Connections Cap ---");	
		ConnectionPool.getConnectionPool().getConnection();
		Thread.sleep(1000);
		
		LOGGER.info("--- Release Connection To Make Room For Waiting Connection ---");
		BiFunction<CopyOnWriteArrayList<Connection>, Integer, Connection> filterByConnectionNumber = (connections, number) -> {
			for (Connection connection: connections) {
				if (connection.getConnectionNumber() == number) {
					return connection;
				}
			}
			return null;
		};
		Connection connectionToRelease = filterByConnectionNumber.apply(ConnectionPool.getConnectionPool().getConnections(), 2);
		
		ConnectionPool.getConnectionPool().releaseConnection(connectionToRelease);
		Thread.sleep(1000);
		
		LOGGER.info("--- Release All Active Connections ---");
		for (Connection connection: ConnectionPool.getConnectionPool().getConnections()) {
			ConnectionPool.getConnectionPool().releaseConnection(connection);
		}
		
		Thread.sleep(1000);
		
		ConnectionPool.getExecutor().shutdown();
		ConnectionPool.getConnectionPool().interrupt();
		LOGGER.info("--- Connection Pool Shut Down ---");
	}

}
