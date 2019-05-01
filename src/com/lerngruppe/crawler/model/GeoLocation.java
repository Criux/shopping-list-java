package com.lerngruppe.crawler.model;

public class GeoLocation {

	double latitude;
	double longitude;
	public GeoLocation(String coords) {
		String[] parts = coords.split("\\,");
		latitude=Double.valueOf(parts[0].trim());
		longitude=Double.valueOf(parts[1].trim());
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	@Override
	public String toString() {
		return latitude + "," + longitude;
	}
	
}
