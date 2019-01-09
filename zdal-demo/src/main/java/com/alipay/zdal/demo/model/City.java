package com.alipay.zdal.demo.model;

public class City {
	
	private int id;
	private String cityName;

	public City(int id, String cityName) {
		this.id = id;
		this.cityName = cityName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

}