package com.lerngruppe.rest.example.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

import com.lerngruppe.crawler.model.Product;
import com.lerngruppe.crawler.model.Store;
import com.lerngruppe.crawler.utils.ExtractReweItems;
import com.lerngruppe.rest.example.dao.CustomerDAO;
import com.lerngruppe.rest.example.dao.impl.CustomerDAOImpl;
import com.lerngruppe.rest.example.dao.impl.ProductDAOImpl;
import com.lerngruppe.rest.example.model.Customer;

@Path("example")
public class TomcatJNDIExample {
	private Logger logger = Logger.getLogger(TomcatJNDIExample.class);	
	
	@Path("status")
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String getStatus() {
		logger.info("Inside getStatus()...");
		return "TomcatJNDIExample Status is OK...";
	}

	@GET
	@Path("getcustomer")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCustomer(@DefaultValue("0") @QueryParam("id") int id) {

		CustomerDAO daoImpl = new CustomerDAOImpl();
		logger.info("Inside getCustomer...");
		
		Response resp = daoImpl.getCustomer(id);
		return resp;
	}

	@POST
	@Path("addcustomer")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createCustomer(Customer customer) {

		CustomerDAO daoImpl = new CustomerDAOImpl();
		logger.info("Inside createCustomer...");
		
		Response resp = daoImpl.createCustomer(customer);
		return resp;
	}
	
	@PUT
	@Path("updatecustomer")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateCustomer(Customer customer) {

		CustomerDAO daoImpl = new CustomerDAOImpl();
		logger.info("Inside createCustomer...");
		
		Response resp = daoImpl.updateCustomer(customer);
		return resp;
	}
	
	@DELETE
	@Path("deletecustomer")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteCustomer(@DefaultValue("0") @QueryParam("id") int id) {

		CustomerDAO daoImpl = new CustomerDAOImpl();
		logger.info("Inside deleteCustomer...");
		
		Response resp = daoImpl.deleteCustomer(id);
		return resp;
	}
	
	@GET
	@Path("showallcustomers")
	@Produces(MediaType.APPLICATION_JSON)
	public Response showAllCustomers() throws JsonGenerationException,
		JsonMappingException, IOException {

		CustomerDAO daoImpl = new CustomerDAOImpl();
		logger.info("Inside showAllCustomers...");
		Response resp = daoImpl.getAllCustomers();
		
		return resp;
	}
	@GET
	@Path("products")
	@Produces(MediaType.APPLICATION_JSON)
	public Response showAllProducts() {
		ProductDAOImpl dao = new ProductDAOImpl();
		logger.info("Inside showAllProducts...");
		return dao.getAllProducts();
	}
	@GET
	@Path("products/bielefeld")
	@Produces(MediaType.TEXT_HTML)
	public String updateBielefeldProducts() throws IOException {
		Thread myT =new Thread(new Runnable() {

			@Override
			public void run() {
				ExtractReweItems rewe = new ExtractReweItems();
				for (Store store : rewe.getStoreInfoFromPage("https://www.rewe.de/marktseite/bielefeld/",
						"nordrhein-westfalen")) {
					rewe.db.deleteTransactionalForStore(store.getId());
					System.out.println("working on store:"+store.getId());
					List<Product> productList;
					try {
						productList = rewe
								.getProductsFromCategory("https://shop.rewe.de/productList?objectsPerPage=250", store);
						System.out.println("Total products found: " + productList.size());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//productList = productList.stream().distinct().collect(Collectors.toList());
							
					ExtractReweItems.existingProductIds = rewe.db.getAllProductIds();
				}
				
			}
			
		});
		myT.start();
		return "Job started! It can take up to several minutes to complete.";
	}
}
