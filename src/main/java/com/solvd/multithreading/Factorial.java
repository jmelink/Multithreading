package com.solvd.multithreading;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Factorial implements Runnable {
	private Integer factorial;
	private Integer result;
	private final static Logger logger = LogManager.getLogger(Factorial.class.getClass());
	
	public Factorial() {
		
	}
	
	public Factorial(Integer factorial) {
		this.factorial = factorial;
	}
	
	public Integer getFactorial() {
		return factorial;
	}

	public void setFactorial(Integer factorial) {
		this.factorial = factorial;
	}

	public Integer getResult() {
		return result;
	}

	public void setResult(Integer result) {
		this.result = result;
	}

	public Integer calculateFactorial(Integer value) {
		if (value > 1) {
			logger.info("Factorial calculation currently using: " + value);
			return value * calculateFactorial(value - 1);
		} else if (value == 1) {
			logger.info("Factorial calculation currently using: " + value);
			return value;
		} else {
			return 0;
		}
	}
	
	@Override
	public void run() {
		setResult(calculateFactorial(getFactorial()));
	}
}
