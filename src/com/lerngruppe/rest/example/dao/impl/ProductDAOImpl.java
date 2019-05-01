package com.lerngruppe.rest.example.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

import com.lerngruppe.rest.example.model.Customer;
import com.lerngruppe.rest.example.model.Product_example;
import com.lerngruppe.rest.example.util.Database_example;
import com.lerngruppe.rest.utils.StatusMessage;

public class ProductDAOImpl {

	private DataSource datasource = Database_example.getDataSource();
	private Logger logger = Logger.getLogger(ProductDAOImpl.class);

	public Response getAllProducts() {
		Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    
		List<Product_example> allProducts = new ArrayList<>();
		String sql = "select * from products";
		
		try {
			conn = datasource.getConnection();
			ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      
      while (rs.next()) {
      	Product_example prd = new Product_example();
      	prd.setCategoryId(rs.getInt("category_id"));
      	prd.setCreated(rs.getTimestamp("created"));
      	prd.setDescription(rs.getString("description"));
      	prd.setModified(rs.getTimestamp("modified"));
      	prd.setName(rs.getString("name"));
      	prd.setPrice(rs.getDouble("price"));
      	prd.setProductId(rs.getInt("id"));
      	
      	allProducts.add(prd);
      }
      
      if (allProducts.isEmpty()) {
      	logger.error("No Product Exists...");
  			StatusMessage statusMessage = new StatusMessage();
  			statusMessage.setStatus(Status.NOT_FOUND.getStatusCode());
  			statusMessage.setMessage("No Product Exists...");
  			return Response.status(404).entity(statusMessage).build();
      }
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					logger.error("Error closing resultset: " + e.getMessage());
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					logger.error("Error closing PreparedStatement: " + e.getMessage());
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					logger.error("Error closing connection: " + e.getMessage());
					e.printStackTrace();
				}
			}
		}
		return Response.status(200).entity(allProducts).build();
	}
}
