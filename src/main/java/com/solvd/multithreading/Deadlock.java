package com.solvd.multithreading;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Deadlock {
	
	private final static Logger logger = LogManager.getLogger(Deadlock.class.getClass());
	
	public static void main(String[] args) {
		Object can = new Object();
		Object canOpener = new Object();
		
		Thread thread1 = new Thread() {
			public void run() {
				synchronized(can) {
					logger.info("Can locked to thread: " + this.getName());
					try {
						Thread.sleep(50); 
			        } catch (InterruptedException e) {}
					
					synchronized(canOpener) {
						logger.info("Can opener locked to thread: " + this.getName());
					}
				}
			}
		};
		
		Thread thread2 = new Thread() {
			public void run() {
				synchronized(canOpener) {
					logger.info("Can opener locked to thread: " + this.getName());
					try {
						Thread.sleep(50); 
			        } catch (InterruptedException e) {}
					
					synchronized(can) {
						logger.info("Can locked to thread: " + this.getName());
					}
				}
			}
		};
		
		thread1.start();
		thread2.start();
	}
}
