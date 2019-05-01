package com.lerngruppe.crawler.parser;

import java.util.HashMap;
import java.util.Map;

import com.lerngruppe.crawler.model.Store;


public class StoreParser {

	public static Map<String,Object> getInsertMap(Store store){
		Map<String, Object> map= new HashMap<>();
		map.put("ID", store.getId());
		map.put("NAME", store.getName());
		map.put("ADDRESS", store.getAddress().toString());
		map.put("CLOSING_TIME", store.getOpeningHours());
		map.put("GEOLOCATION",store.getMapLocation().toString());
		map.put("STATE",store.getAddress().getState());
		return map;
	}
	public static Store parseDBStore(Map<String,String> map) {	
		return new Store(map.get("ID"),map.get("NAME"),map.get("ADDRESS"),map.get("STATE"),map.get("CLOSING_TIME"),map.get("GEOLOCATION"),"");
	}
}
