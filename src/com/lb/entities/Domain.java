package com.lb.entities;

import java.io.Serializable;

public class Domain implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5875021578078702944L;
	private int id;
	private String domain;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	@Override
	public String toString() {
		return "Domain [id=" + id + ", domain=" + domain + "]";
	}
}
