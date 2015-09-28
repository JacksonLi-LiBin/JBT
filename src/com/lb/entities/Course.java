package com.lb.entities;

import java.io.Serializable;

public class Course implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4993656678058524705L;
	private String courseNumber;
	private String courseName;
	private String cycleNumber;

	public Course(String courseNumber, String courseName, String cycleNumber) {
		super();
		this.courseNumber = courseNumber;
		this.courseName = courseName;
		this.cycleNumber = cycleNumber;
	}

	public Course() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getCourseNumber() {
		return courseNumber;
	}

	public void setCourseNumber(String courseNumber) {
		this.courseNumber = courseNumber;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getCycleNumber() {
		return cycleNumber;
	}

	public void setCycleNumber(String cycleNumber) {
		this.cycleNumber = cycleNumber;
	}

	@Override
	public String toString() {
		return "{courseNumber:" + courseNumber + ", courseName:" + courseName
				+ ", cycleNumber:" + cycleNumber + "}";
	}

}
