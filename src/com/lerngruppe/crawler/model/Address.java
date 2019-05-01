package com.lerngruppe.crawler.model;

public class Address {

	String street;
	String houseNumber;
	String postalCode;
	String city;
	String state;
	public Address(String fullName,String state) {
		String[] parts=fullName.split(" ");
		street=parts[0];
		houseNumber=parts[1].replace(",", "");
		postalCode=parts[2];
		city=parts[3];
		this.state=state;
		
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getHouseNumber() {
		return houseNumber;
	}
	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	@Override
	public String toString() {
		return street + " " + houseNumber + ", " + postalCode + " "
				+ city;
	}
}
