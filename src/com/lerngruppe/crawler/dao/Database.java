package com.lerngruppe.crawler.dao;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.lerngruppe.rest.example.util.Database_example;

public class Database {

	static Logger logger = LogManager.getLogger(Database_example.class);
	private static DataSource ds = null;
	private static String resourceName="jdbc/shoppingList_prod";

	static {
		logger.info("Inside Database() static method... ");
		Context context = null;
		try {
			context = new InitialContext();
			ds = (DataSource)context.lookup("java:comp/env/"+Database.resourceName);
		} catch (NamingException e) {
			logger.error("Unable to get Datasource...");
			e.printStackTrace();
		}
	}
	
	public static DataSource getDataSource() {
		return ds;
	}
}
