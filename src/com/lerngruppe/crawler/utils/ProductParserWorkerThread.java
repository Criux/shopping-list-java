package com.lerngruppe.crawler.utils;

import java.util.Collection;
import java.util.function.Function;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class ProductParserWorkerThread<T, E> implements Runnable {

	private static final Logger logger = LogManager.getLogger(ProductParserWorkerThread.class);
	private Function<T, ? extends Collection<E>> f;
	private T url;
	private Collection<E> list;

	public ProductParserWorkerThread(Function<T, ? extends Collection<E>> f, T url, Collection<E> result) {
		this.f = f;
		this.url = url;
		this.list = result;
	}

	@Override
	public void run() {
		parse();
	}

	private void parse() {
		list.addAll(f.apply(url));
		logger.info("Parsed page: " + url);
	}
}
