package com.solvd.multithreading;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Decrement implements Runnable {
	private Integer start;
	private Integer current;
	private Integer end;
	private final static Logger logger = LogManager.getLogger(Decrement.class.getClass());
	
	public Decrement() {
		
	}
	
	public Decrement(Integer start, Integer end) {
		this.start = start;
		this.current = start;
		this.end = end;
	}
	
	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getCurrent() {
		return current;
	}

	public void setCurrent(Integer current) {
		this.current = current;
	}

	public Integer getEnd() {
		return end;
	}

	public void setEnd(Integer end) {
		this.end = end;
	}

	@Override
	public void run() {
		while (getStart() > getEnd()) {
			logger.info("Subtraction Counter: " + getCurrent());
			setCurrent(getCurrent() - 1);
		}
	}
}
