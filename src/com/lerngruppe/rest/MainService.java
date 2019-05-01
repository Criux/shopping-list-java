package com.lerngruppe.rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.lerngruppe.crawler.model.Store;
import com.lerngruppe.rest.provider.StoreProvider;
import com.lerngruppe.rest.utils.StatusMessage;

@Path("/")
public class MainService {
	private Logger logger = LogManager.getLogger(MainService.class);
	@GET
	@Path("stores")
	@Produces(MediaType.APPLICATION_JSON+";charset=UTF-8")
	public Response getAllStores() {
		List<Store> stores=StoreProvider.getInstance().getAllStores();
		StatusMessage statusMessage = new StatusMessage();
		Response resp=null;
		if(stores==null||stores.size()==0) {
			statusMessage.setStatus(Status.NOT_FOUND.getStatusCode());
			statusMessage.setMessage("Could not get stores or no stores were found.");
			resp=Response.status(404).entity(statusMessage)
					.build();
		}else {
			resp=Response.status(200).entity(stores).build();
		}
			
		return resp;
	}
	@GET
	@Path("store")
	@Produces(MediaType.APPLICATION_JSON+";charset=UTF-8")
	public Response getStore( @QueryParam("store_id") String id) {
		Store result=StoreProvider.getInstance().getSingleStore(id);
		StatusMessage statusMessage = new StatusMessage();
		Response resp=null;
		if(result==null) {
			statusMessage.setStatus(Status.NOT_FOUND.getStatusCode());
			statusMessage.setMessage("Could not find a store with the id:"+id);
			resp=Response.status(404).entity(statusMessage)
					.build();
		}else {
			resp=Response.status(200).entity(result).build();
		}
			
		return resp;
	}
}
