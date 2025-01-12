package com.solvd.multithreading;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeoutException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {
	
	private final static Logger logger = LogManager.getLogger(Increment.class.getClass());
	
	public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {
		ExecutorService executor = Executors.newFixedThreadPool(3);
		Increment incrementThread = new Increment(30, 100);
		Decrement decrementThread = new Decrement(75, 10);
		Factorial factorialThread = new Factorial(10);
		FutureTask<String> factorialTask = new FutureTask<String>(factorialThread, "Factorial calculation complete: ");
		
		executor.submit(incrementThread);
		executor.submit(decrementThread);
		executor.submit(factorialTask);
		
		while (!factorialTask.isDone()) {
			logger.info("Calculating factorial...");
			Thread.sleep(1);
		}
		
		logger.info(factorialTask.get() + factorialThread.getResult());
		
		executor.shutdown();
	}
}
