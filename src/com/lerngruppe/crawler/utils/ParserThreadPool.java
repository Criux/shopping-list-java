package com.lerngruppe.crawler.utils;

import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

//https://www.journaldev.com/1069/threadpoolexecutor-java-thread-pool-example-executorservice
public class ParserThreadPool<T,E> {

	//constructor for product parser worker
	public ParserThreadPool(Function<T, ? extends Collection<E>> f, Collection<T> inputList,Collection<E> out) {
		ExecutorService executor = Executors.newFixedThreadPool(5);
		Function<T, ? extends Collection<E>> fa = f;
		for (T singleTask:inputList) {
			Runnable worker = new ProductParserWorkerThread<T,E>(fa,singleTask, out);
			executor.execute(worker);
			try {
				TimeUnit.MILLISECONDS.sleep(25);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		executor.shutdown();
		while (!executor.isTerminated()) {
		}
	}
}
