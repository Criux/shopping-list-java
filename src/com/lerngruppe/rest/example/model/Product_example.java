package com.lerngruppe.rest.example.model;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;

import org.codehaus.jackson.annotate.JsonProperty;

public class Product_example {

	private int productId;
	private String name;
	private String description;
	private double price;
	private int categoryId;
	private Timestamp created;
	private Timestamp modified;
	@JsonProperty(value = "id")
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	@JsonProperty(value = "name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@JsonProperty(value = "description")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@JsonProperty(value = "price")
	public double getPrice() {
		return price;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}
	@JsonProperty(value = "category_id")
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	@JsonProperty(value = "created")
	public Timestamp getCreated() {
		return created;
	}
	public void setCreated(Timestamp created) {
		this.created = created;
	}
	@JsonProperty(value = "modified")
	public Timestamp getModified() {
		return modified;
	}
	public void setModified(Timestamp modified) {
		this.modified = modified;
	}
	
	
	
}
