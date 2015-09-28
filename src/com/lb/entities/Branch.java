package com.lb.entities;

import java.io.Serializable;

public class Branch implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7695232977569982382L;
	private String branchId;

	public Branch() {
		super();
	}

	public Branch(String branchId) {
		super();
		this.branchId = branchId;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	@Override
	public String toString() {
		return "{branchId:" + branchId + "}";
	}

}
