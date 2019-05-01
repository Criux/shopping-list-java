package com.lerngruppe.rest.provider;

import java.util.List;
import java.util.stream.Collectors;

import com.lerngruppe.crawler.dao.DatabaseQueries;
import com.lerngruppe.crawler.model.Store;

public class StoreProvider {

	private List<Store> stores;
	private DatabaseQueries db;
	private static StoreProvider instance=null;
	private StoreProvider() {
		db= new DatabaseQueries();
	};
	public static StoreProvider getInstance() {
		if(instance==null) {
			instance = new StoreProvider();
		}
		return instance;
	}
	public List<Store> getAllStores(){
		if(stores==null) {
			stores=db.getAllStores();
		}
		return stores;
	}
	public Store getSingleStore(String storeId) {
		return db.getStoreFromId(storeId);
	}
}
