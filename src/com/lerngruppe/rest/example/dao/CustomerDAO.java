package com.lerngruppe.rest.example.dao;

import javax.ws.rs.core.Response;

import com.lerngruppe.rest.example.model.Customer;

public interface CustomerDAO {
	
	public Response getCustomer(int id);
	public Response createCustomer(Customer customer);
	public Response updateCustomer(Customer customer);
	public Response deleteCustomer(int id);
	public Response getAllCustomers();
	
}
