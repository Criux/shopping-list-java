package com.lerngruppe.crawler.model;

import java.time.LocalDate;

public class Product {

	String id;
	String name;
	String brand;
	boolean isActive;
	String quantityInfo;
	String nutrition;
	String description2;
	String description3;
	String pictureUrl;
	String labelText;
	Store store;
	double originalPrice;
	double currentPrice;
	boolean onSale;
	LocalDate saleUntil;

	public Product(String id) {
		super();
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getQuantityInfo() {
		return quantityInfo;
	}

	public void setQuantityInfo(String quantityInfo) {
		this.quantityInfo = quantityInfo;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	public String getNutrition() {
		return nutrition;
	}

	public void setNutrition(String nutrition) {
		this.nutrition = nutrition;
	}

	public String getDescription2() {
		return description2;
	}

	public void setDescription2(String description2) {
		this.description2 = description2;
	}

	public String getDescription3() {
		return description3;
	}

	public void setDescription3(String description3) {
		this.description3 = description3;
	}

	public String getLabelText() {
		return labelText;
	}

	public void setLabelText(String labelText) {
		this.labelText = labelText;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public double getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(double originalPrice) {
		this.originalPrice = originalPrice;
	}

	public double getCurrentPrice() {
		return currentPrice;
	}

	public void setCurrentPrice(double currentPrice) {
		this.currentPrice = currentPrice;
	}

	public boolean isOnSale() {
		return onSale;
	}

	public void setOnSale(boolean onSale) {
		this.onSale = onSale;
	}

	public LocalDate getSaleUntil() {
		return saleUntil;
	}

	public void setSaleUntil(LocalDate saleUntil) {
		this.saleUntil = saleUntil;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Product other = (Product) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", brand=" + brand + ", isActive=" + isActive
				+ ", quantityInfo=" + quantityInfo + ", nutrition=" + nutrition + ", description2=" + description2
				+ ", description3=" + description3 + ", pictureUrl=" + pictureUrl + ", labelText=" + labelText
				+ ", store=" + store + ", originalPrice=" + originalPrice + ", currentPrice=" + currentPrice
				+ ", onSale=" + onSale + ", saleUntil=" + saleUntil + "]";
	}
	
}
