package com.lerngruppe.rest.example.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

import com.lerngruppe.rest.example.dao.CustomerDAO;
import com.lerngruppe.rest.example.model.Customer;
import com.lerngruppe.rest.example.util.Database_example;
import com.lerngruppe.rest.utils.StatusMessage;

public class CustomerDAOImpl implements CustomerDAO {
	private DataSource datasource = Database_example.getDataSource();
	private Logger logger = Logger.getLogger(CustomerDAOImpl.class);
	
	@Override
	public Response getCustomer(int id) {
		Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
		
    Customer customer = null;
		String sql = "select customer_id, first_name, last_name, address, city, "
				+ "state, zip_code, is_active from Customers where customer_id = ?";
		
		try {
			conn = datasource.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
      rs = ps.executeQuery();
      
      if (rs.next()) {
      	customer = new Customer();
      	customer.setCustomerId(rs.getInt("customer_id"));
      	customer.setFirstName(rs.getString("first_name"));
      	customer.setLastName(rs.getString("last_name"));
      	customer.setAddress(rs.getString("address"));
      	customer.setCity(rs.getString("city"));
      	customer.setState(rs.getString("state"));
      	customer.setZipCode(rs.getString("zip_code"));
      	customer.setIsActive(rs.getInt("is_active"));
      } else {
  			logger.error(String.format("Customer with ID of %d is not found.", id));
  			StatusMessage statusMessage = new StatusMessage();
  			statusMessage.setStatus(Status.NOT_FOUND.getStatusCode());
  			statusMessage.setMessage(String.format("Customer with ID of %d is not found.", id));
  			return Response.status(404).entity(statusMessage).build();
      }
		} catch (SQLException e) {
			logger.error("Error: " + e.getMessage());
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
		return Response.status(200).entity(customer).build();
	}

	@Override
	public Response createCustomer(Customer customer) {
		Connection conn = null;
    PreparedStatement ps = null;
    Statement stmt = null;
    ResultSet rs = null;
    StatusMessage statusMessage = null;
    int autoID = -1;
		
		String sql = "insert into customers (first_name, last_name, address, city, "
				+ "state, zip_code, is_active) values (?,?,?,?,?,?,?)";
		
		try {
			conn = datasource.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, customer.getFirstName());
			ps.setString(2, customer.getLastName());
			ps.setString(3, customer.getAddress());
			ps.setString(4, customer.getCity());
			ps.setString(5, customer.getState());
			ps.setString(6, customer.getZipCode());
			ps.setInt(7, customer.getIsActive());
			
      int rows = ps.executeUpdate();
      
      if (rows == 0) {
  			logger.error("Unable to create customer...");
  			statusMessage = new StatusMessage();
  			statusMessage.setStatus(Status.NOT_FOUND.getStatusCode());
  			statusMessage.setMessage("Unable to create customer...");
  			return Response.status(404).entity(statusMessage).build();
      }
      
      stmt = conn.createStatement();
      rs = stmt.executeQuery("select LAST_INSERT_ID()");

      if (rs.next()) {
          autoID = rs.getInt(1);
          customer.setCustomerId(autoID);
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
		return Response.status(200).entity(customer).build();
	}

	@Override
	public Response updateCustomer(Customer customer) {
		Connection conn = null;
    PreparedStatement ps = null;
		
		String sql = "update customers set first_name=?, last_name=?, "
				+ "address=?, city=?, state=?, zip_code=?, is_active=? "
				+ "where customer_id = ?";
		
		try {
			conn = datasource.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, customer.getFirstName());
			ps.setString(2, customer.getLastName());
			ps.setString(3, customer.getAddress());
			ps.setString(4, customer.getCity());
			ps.setString(5, customer.getState());
			ps.setString(6, customer.getZipCode());
			ps.setInt(7, customer.getIsActive());
			ps.setInt(8, customer.getCustomerId());
			
      int rows = ps.executeUpdate();
      
      if (rows == 0) {
  			logger.error("Unable to update customer...");
  			StatusMessage statusMessage = new StatusMessage();
  			statusMessage.setStatus(Status.NOT_FOUND.getStatusCode());
  			statusMessage.setMessage("Unable to update customer...");
  			return Response.status(404).entity(statusMessage).build();
      }
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
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
		return Response.status(200).entity(customer).build();
	}

	@Override
	public Response deleteCustomer(int id) {
		Connection conn = null;
    PreparedStatement ps = null;
    StatusMessage statusMessage = null;
		
		String sql = "delete from customers where customer_id = ?";
		
		try {
			conn = datasource.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
      int rows = ps.executeUpdate();
      
      if (rows == 0) {
  			logger.error(String.format("Unable to DELETE customer with ID of %d...", id));
  			statusMessage = new StatusMessage();
  			statusMessage.setStatus(Status.NOT_FOUND.getStatusCode());
  			statusMessage.setMessage(String.format("Unable to DELETE customer with ID of %d...", id));
  			return Response.status(404).entity(statusMessage).build();
      }
		} catch (SQLException e) {
			logger.error("Error: " + e.getMessage());
			e.printStackTrace();
		} finally {
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
		
		statusMessage = new StatusMessage();
		statusMessage.setStatus(Status.OK.getStatusCode());
		statusMessage.setMessage(String.format("Successfully deleted customer with ID of %d...", id));
		return Response.status(200).entity(statusMessage).build();
	}

	@Override
	public Response getAllCustomers() {
		Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    
		List<Customer> allCustomers = new ArrayList<Customer>();
		String sql = "select customer_id, first_name, last_name, address, city, "
								+ "state, zip_code, is_active from Customers";
		
		try {
			conn = datasource.getConnection();
			ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      
      while (rs.next()) {
      	Customer cust = new Customer();
      	cust.setCustomerId(rs.getInt("customer_id"));
      	cust.setFirstName(rs.getString("first_name"));
      	cust.setLastName(rs.getString("last_name"));
      	cust.setAddress(rs.getString("address"));
      	cust.setCity(rs.getString("city"));
      	cust.setState(rs.getString("state"));
      	cust.setZipCode(rs.getString("zip_code"));
      	cust.setIsActive(rs.getInt("is_active"));
      	allCustomers.add(cust);
      }
      
      if (allCustomers.isEmpty()) {
      	logger.error("No Customers Exists...");
  			StatusMessage statusMessage = new StatusMessage();
  			statusMessage.setStatus(Status.NOT_FOUND.getStatusCode());
  			statusMessage.setMessage("No Customers Exists...");
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
		return Response.status(200).entity(allCustomers).build();
	}
}
