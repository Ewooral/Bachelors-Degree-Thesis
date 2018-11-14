package com.sw.helpapp.classes;

public class Contact {

	private String name;
	
	private String phone;
	

	public Contact(String name, String phone) {
		this.name = name;
		this.phone = phone;
	}
	

	public Contact() {
		super();
		// TODO Auto-generated constructor stub
	}


	public String getName() {
		return name;
	}

	public String getPhone() {
		return phone;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	
}
