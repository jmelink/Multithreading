package com.solvd.multithreading;

import java.util.concurrent.ArrayBlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConnectionPool {
	private final static Integer MAX_CONNECTIONS = 5;
	private ArrayBlockingQueue<Connection> connections;
	private final static ConnectionPool INSTANCE = new ConnectionPool();
	
	private final static Logger LOGGER = LogManager.getLogger(ConnectionPool.class.getClass());
	
	private ConnectionPool() {
		try {
			this.connections = new ArrayBlockingQueue<Connection>(getMaxConnections());
			for (int i = 0; i < getMaxConnections(); i++) {
				connections.put(new Connection(this));
			}
		} catch (InterruptedException e) {
			LOGGER.error("Error when getting connection for  " + Thread.currentThread().getName(), e.getMessage());
		}	
	}

	public static ConnectionPool getConnectionPool() {
		return INSTANCE;
	}
	
	public Integer getMaxConnections() {
		return MAX_CONNECTIONS;
	}
	
	public Connection getConnection() {
		try {
			return connections.take();
		} catch (InterruptedException e) {
			LOGGER.error("Error when getting connection for  " + Thread.currentThread().getName() + "'s connection.", e.getMessage());
			throw new RuntimeException(e);
		}
	}
	
	public void releaseConnection(Connection connection) {
		try {
			connections.put(connection);
		} catch (InterruptedException e) {
			LOGGER.error("Error when releasing  " + Thread.currentThread().getName() + "'s connection.", e.getMessage());
		}
	}
}
