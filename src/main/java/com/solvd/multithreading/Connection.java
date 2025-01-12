package com.solvd.multithreading;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Connection implements Runnable {
	private volatile Boolean running;
	private Integer connectionNumber;
	private static Integer nextConnectionNumber = 0;
	
	private final static Logger LOGGER = LogManager.getLogger(Connection.class.getClass());
	
	public Connection() {
		incrementNextConnectionNumber();
		setConnectionNumber(getNextConnectionNumber());
		setRunning(true);
	}

	public Boolean getRunning() {
		return running;
	}

	public void setRunning(Boolean running) {
		this.running = running;
	}

	public Integer getConnectionNumber() {
		return connectionNumber;
	}

	public void setConnectionNumber(Integer connectionNumber) {
		this.connectionNumber = connectionNumber;
	}

	public static Integer getNextConnectionNumber() {
		return nextConnectionNumber;
	}

	public static void incrementNextConnectionNumber() {
		Connection.nextConnectionNumber++;
	}

	@Override
	public void run() {
		try {
			synchronized (this) {
				while (getRunning()) {
					LOGGER.info("Connection #" + this.getConnectionNumber() + " is running...");
					this.wait();
				}
			}
		} catch (InterruptedException e) {}
	}
	
	public void stop() {
		synchronized (this) {
			setRunning(false);
			this.notifyAll();
		}
	}
}
