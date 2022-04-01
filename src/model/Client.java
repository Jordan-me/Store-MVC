package model;

import java.io.Serializable;

import client_observer.Observer;

public class Client implements Serializable, Observer {

	private static final long serialVersionUID = 1L;
	private String name;
	private boolean getUpdate;
	private String phone;

	public Client(String name, String phone, boolean getUpdate) {
		super();
		this.name = name;
		this.phone = phone;
		this.getUpdate = getUpdate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isGetUpdate() {
		return getUpdate;
	}

	public void setGetUpdate(boolean getUpdate) {
		this.getUpdate = getUpdate;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public String toString() {
		return "Client [name=" + name + ", phone=" + phone + ", getUpdate=" + getUpdate + "]";
	}

	@Override
	public String observerUpdate(String msg) {

		return this.name;

	}

}
