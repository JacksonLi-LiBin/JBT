package com.lb.entities;

import java.io.Serializable;
import java.util.List;

/**
 * friend entity
 * 
 * @author JacksonLi
 * @date 2014/3/13
 */
public class FriendSelected implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7196441308745665102L;
	private String firstName;
	private String lastName;
	private String phone;
	private String email;

	public FriendSelected() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FriendSelected(String firstName, String lastName, String phone,
			String email) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "FriendSelected [firstName=" + firstName + ", lastName="
				+ lastName + ", phone=" + phone + ", email=" + email + "]";
	}

}
