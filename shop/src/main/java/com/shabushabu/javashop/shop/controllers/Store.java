package com.shabushabu.javashop.shop.controllers;

import java.sql.Date;


public class Store {
	private String location;
	private String category;

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return "Location:" + location + " category:" + category ;
	}
}
