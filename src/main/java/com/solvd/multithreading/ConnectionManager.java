package com.solvd.multithreading;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConnectionManager {
	public static void main(String[] args) throws InterruptedException {
		ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
		ExecutorService executor = Executors.newFixedThreadPool(connectionPool.getMaxConnections());

        Runnable connection1 = new Connection(connectionPool);
        Runnable connection2 = new Connection(connectionPool);
        Runnable connection3 = new Connection(connectionPool);
        Runnable connection4 = new Connection(connectionPool);
        Runnable connection5 = new Connection(connectionPool);
        Runnable connection6 = new Connection(connectionPool);

        executor.execute(connection1);
        executor.execute(connection2);
        executor.execute(connection3);
        executor.execute(connection4);
        executor.execute(connection5);
        executor.execute(connection6);

        executor.shutdown();
	}

}
