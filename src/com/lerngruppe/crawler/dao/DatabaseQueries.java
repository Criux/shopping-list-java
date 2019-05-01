package com.lerngruppe.crawler.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.lerngruppe.crawler.model.Store;
import com.lerngruppe.crawler.parser.StoreParser;

public class DatabaseQueries {
	private static final Logger logger = Logger.getLogger(DatabaseQueries.class.getName());
	Connection conn;

	public DatabaseQueries() {
		try {
			conn = Database.getDataSource().getConnection();
		} catch (SQLException e) {
			logger.error("A connection with the database could not be established.");
		}
	}

//	public List<Product> getAllProducts() {
//		List<Product> resultList = new ArrayList<>();
//		String sql = "select * from product";
//		PreparedStatement stmt;
//		try {
//			stmt = conn.prepareStatement(sql);
//			ResultSet rs = stmt.executeQuery();
//			ResultSetMetaData meta = rs.getMetaData();
//			while (rs.next()) {
//				Map<String, String> map = new HashMap<>();
//				for (int idx = 0; idx < meta.getColumnCount(); idx++) {
//					map.put(meta.getColumnName(idx + 1).toUpperCase(), rs.getString(idx + 1).trim());
//				}
//				resultList.add(ProductParser.parseDBProduct(map));
//			}
//
//		} catch (SQLException e) {
//			logger.error(e.getMessage());
//		}
//		return resultList;
//	}
	public List<Store> getAllStores() {
		List<Store> resultList = new ArrayList<>();
		String sql = "select * from store";
		PreparedStatement stmt;
		try {
			stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			ResultSetMetaData meta = rs.getMetaData();
			while (rs.next()) {
				Map<String, String> map = new HashMap<>();
				for (int idx = 0; idx < meta.getColumnCount(); idx++) {
					map.put(meta.getColumnName(idx + 1).toUpperCase(), rs.getString(idx + 1).trim());
				}
				resultList.add(StoreParser.parseDBStore(map));
			}

		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
		return resultList;
	}
	public void insertProductMaster(Map<String, Object> map) {
		String sql = "insert into product (";
		int counter = 0;
		List<Object> valueList = new ArrayList<>();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			if (counter != 0) {
				sql += ",";
			}
			sql += entry.getKey();
			valueList.add(entry.getValue());
			counter++;
		}
		counter = 0;
		sql += ") VALUES (";
		for (int i = 0; i < map.size(); i++) {
			sql += "?";
			if (i != map.size() - 1) {
				sql += ",";
			} else {
				sql += ")";
			}
		}
		sql += " ON DUPLICATE KEY UPDATE ";
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			if (counter != 0) {
				sql += ",";
			}
			sql += entry.getKey() + "=VALUES(" + entry.getKey() + ")";
			counter++;
		}
		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			for (int i = 1; i <= valueList.size(); i++) {
				stmt.setObject(i, valueList.get(i - 1));
			}
			stmt.executeUpdate();
		} catch (SQLException e) {
			if (e.getMessage().contains("Duplicate entry")) {
				handleDuplicateProduct(map);
			} else {
				logger.error(e.getMessage());
			}
		}
		logger.info("Added product in DB with id: " + map.get("ID"));
	}

	private void handleDuplicateProduct(Map<String, Object> map) {
		// can ignore product or update values. Needs to be implemented.

		// https://stackoverflow.com/questions/20255138/sql-update-multiple-records-in-one-query

		logger.debug("Duplicate product was ignored with id: " + map.get("ID"));

	}
	public List<String> getAllProductIds() {
		List<String> resultList = new ArrayList<>();
		String sql = "select id from product";
		PreparedStatement stmt;
		try {
			stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				resultList.add(rs.getString(1));
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
		return resultList;
	}

	public void insertStore(Map<String, Object> map) {
		String sql = "insert into store (";
		int counter = 0;
		List<Object> valueList = new ArrayList<>();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			if (counter != 0) {
				sql += ",";
			}
			sql += entry.getKey();
			valueList.add(entry.getValue());
			counter++;
		}
		counter = 0;
		sql += ") VALUES (";
		for (int i = 0; i < map.size(); i++) {
			sql += "?";
			if (i != map.size() - 1) {
				sql += ",";
			} else {
				sql += ")";
			}
		}
		sql += " ON DUPLICATE KEY UPDATE ";
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			if (counter != 0) {
				sql += ",";
			}
			sql += entry.getKey() + "=VALUES(" + entry.getKey() + ")";
			counter++;
		}
		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			for (int i = 1; i <= valueList.size(); i++) {
				stmt.setObject(i, valueList.get(i - 1));
			}
			stmt.executeUpdate();
		} catch (SQLException e) {
			if (e.getMessage().contains("Duplicate entry")) {
				handleDuplicateProduct(map);
			} else {
				logger.error(e.getMessage());
			}
		}
		logger.info("Added store in DB with id: " + map.get("ID"));
	}
	public void deleteTransactionalForStore(String id) {
		String sql = "delete from store_product where store_id =?";
		PreparedStatement stmt;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, id);
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void insertProductTransactional(Map<String, Object> map) {
		String sql = "insert into store_product (";
		int counter = 0;
		List<Object> valueList = new ArrayList<>();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			if (counter != 0) {
				sql += ",";
			}
			sql += entry.getKey();
			valueList.add(entry.getValue());
			counter++;
		}
		counter = 0;
		sql += ") VALUES (";
		for (int i = 0; i < map.size(); i++) {
			sql += "?";
			if (i != map.size() - 1) {
				sql += ",";
			} else {
				sql += ")";
			}
		}
		sql += " ON DUPLICATE KEY UPDATE ";
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			if (counter != 0) {
				sql += ",";
			}
			sql += entry.getKey() + "=VALUES(" + entry.getKey() + ")";
			counter++;
		}
		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			for (int i = 1; i <= valueList.size(); i++) {
				stmt.setObject(i, valueList.get(i - 1));
			}
			stmt.executeUpdate();
		} catch (SQLException e) {
			if (e.getMessage().contains("Duplicate entry")) {
				handleDuplicateProduct(map);
			} else {
				logger.error(e.getMessage());
			}
		}
		logger.info("Added transactional product data in DB with id: " + map.get("PRODUCT_ID")+" for store:"+map.get("STORE_ID"));
		
	}

	public Store getStoreFromId(String storeId) {
		String sql ="select * from store where id=?";
		try {
			PreparedStatement stmt=conn.prepareStatement(sql);
			stmt.setString(1, storeId);
			ResultSet rs=stmt.executeQuery();
			ResultSetMetaData meta = rs.getMetaData();
			while (rs.next()) {
				Map<String, String> map = new HashMap<>();
				for (int idx = 0; idx < meta.getColumnCount(); idx++) {
					map.put(meta.getColumnName(idx + 1).toUpperCase(), rs.getString(idx + 1).trim());
				}
				return StoreParser.parseDBStore(map);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
}
