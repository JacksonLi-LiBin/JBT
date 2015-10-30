package com.lb.entities;

import java.io.Serializable;

public class Area implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8039423833853318868L;
	private int id;
	private String area;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	@Override
	public String toString() {
		return "Area [id=" + id + ", area=" + area + "]";
	}

}
