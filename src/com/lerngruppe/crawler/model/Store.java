package com.lerngruppe.crawler.model;

public class Store {

	String id;
	String name;
	Address address;
	String openingHours;
	GeoLocation mapLocation;
	String originUrl;
	
	public Store(String id,String name,String addressFull,String state,String openingHours,String geolocationFull,String originUrl) {
		this.id=id;
		this.name=name;
		this.address= new Address(addressFull,state);
		this.openingHours=openingHours;
		this.mapLocation=new GeoLocation(geolocationFull);
		this.originUrl=originUrl;
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
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	public String getOpeningHours() {
		return openingHours;
	}
	public void setOpeningHours(String openingHours) {
		this.openingHours = openingHours;
	}
	public GeoLocation getMapLocation() {
		return mapLocation;
	}
	public void setMapLocation(GeoLocation mapLocation) {
		this.mapLocation = mapLocation;
	}
	public String getOriginUrl() {
		return originUrl;
	}
	public void setOriginUrl(String originUrl) {
		this.originUrl = originUrl;
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
		Store other = (Store) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Store [id=" + id + ", name=" + name + ", address=" + address + "]";
	}
	
}
