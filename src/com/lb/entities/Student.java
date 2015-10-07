package com.lb.entities;

import java.io.Serializable;
import java.util.List;

public class Student implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7842526731571143318L;
	private String stuId;
	private String firstName;
	private String lastName;
	private String email;
	private List<Course> courseList;
	private List<Branch> branchList;

	public Student() {
		super();
	}

	public Student(String stuId, List<Course> courseList) {
		super();
		this.stuId = stuId;
		this.courseList = courseList;
	}

	public Student(String stuId, String firstName, String lastName, String email, List<Course> courseList,
			List<Branch> branchList) {
		super();
		this.stuId = stuId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.courseList = courseList;
		this.branchList = branchList;
	}

	public String getStuId() {
		return stuId;
	}

	public void setStuId(String stuId) {
		this.stuId = stuId;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Course> getCourseList() {
		return courseList;
	}

	public void setCourseList(List<Course> courseList) {
		this.courseList = courseList;
	}

	public List<Branch> getBranchList() {
		return branchList;
	}

	public void setBranchList(List<Branch> branchList) {
		this.branchList = branchList;
	}

	@Override
	public String toString() {
		return "[stuId=" + stuId + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
				+ ", courseList=" + courseList + ", branchList=" + branchList + "]";
	}

}
