package com.lerngruppe.crawler.utils;

import java.util.concurrent.TimeUnit;

public class Stopwatch {

	private long startTime;

	public Stopwatch(long startTime) {
		this.startTime = startTime;
	}

	public Stopwatch() {

	}

	public void start() {
		startTime = System.currentTimeMillis();
	}

	public String stop() {
		Long duration = System.currentTimeMillis() - startTime;
		return "Total time: " + String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(duration),
				TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
	}
}
