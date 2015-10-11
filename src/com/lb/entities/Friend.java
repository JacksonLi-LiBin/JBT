package com.lb.entities;

import java.io.Serializable;
import java.util.List;

/**
 * friend entity
 * 
 * @author JacksonLi
 * @date 2014/3/13
 */
public class Friend implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1327126337465877834L;
	private String firstName;
	private String lastName;
	private List<String> phones;
	private List<String> emails;

	public Friend() {
		super();
	}

	public Friend(String firstName, String lastName, List<String> phones, List<String> emails) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.phones = phones;
		this.emails = emails;
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

	public List<String> getPhones() {
		return phones;
	}

	public void setPhones(List<String> phones) {
		this.phones = phones;
	}

	public List<String> getEmails() {
		return emails;
	}

	public void setEmails(List<String> emails) {
		this.emails = emails;
	}

	@Override
	public String toString() {
		return "Friend [firstName=" + firstName + ", lastName=" + lastName + ", phones=" + phones + ", emails=" + emails
				+ "]";
	}

}
