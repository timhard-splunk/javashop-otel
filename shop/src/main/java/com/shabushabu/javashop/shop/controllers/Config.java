package com.shabushabu.javashop.shop.controllers;

public class Config {
    private String lb;

	public String getLb() {
		return lb;
	}

	public void setLb(String lb) {
		this.lb = lb;
	}

	@Override
	public String toString() {
		return "Load Balancing Enabled:" + lb;
	}

	public String getLBStatus() {
		if (Boolean.parseBoolean(lb)){
		return "Enabled";
		} else {
        return "Disabled";
		}
	}
}
