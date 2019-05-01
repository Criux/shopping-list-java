package com.lerngruppe.rest.example.util;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

public class Database_example {
	static Logger logger = Logger.getLogger(Database_example.class);
	private static DataSource ds = null;

	static {
		logger.info("Inside Database() static method... ");
		Context context = null;
		try {
			context = new InitialContext();
			ds = (DataSource)context.lookup("java:comp/env/jdbc/tutorialdb");
		} catch (NamingException e) {
			logger.error("Unable to get Datasource...");
			e.printStackTrace();
		}
	}
	
	public static DataSource getDataSource() {
		return ds;
	}
}
