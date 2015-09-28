package com.lb.entities;

public class CourseSyllabus {
	private String meetDate;
	private String meetStart;
	private String meetEnd;
	private String meetNum;
	private String meetBranch;
	private String meetBuildingNum;
	private String meetBuildingName;
	private String meetClassNum;
	private String meetTeacher;
	private String meetStatus;
	private String meetTopic;
	private String meetClassName;

	public CourseSyllabus() {
		super();
	}

	public CourseSyllabus(String meetDate, String meetStart, String meetEnd,
			String meetNum, String meetBranch, String meetBuildingNum,
			String meetBuildingName, String meetClassNum, String meetTeacher,
			String meetStatus, String meetTopic, String meetClassName) {
		super();
		this.meetDate = meetDate;
		this.meetStart = meetStart;
		this.meetEnd = meetEnd;
		this.meetNum = meetNum;
		this.meetBranch = meetBranch;
		this.meetBuildingNum = meetBuildingNum;
		this.meetBuildingName = meetBuildingName;
		this.meetClassNum = meetClassNum;
		this.meetTeacher = meetTeacher;
		this.meetStatus = meetStatus;
		this.meetTopic = meetTopic;
		this.meetClassName = meetClassName;
	}

	public String getMeetDate() {
		return meetDate;
	}

	public void setMeetDate(String meetDate) {
		this.meetDate = meetDate;
	}

	public String getMeetStart() {
		return meetStart;
	}

	public void setMeetStart(String meetStart) {
		this.meetStart = meetStart;
	}

	public String getMeetEnd() {
		return meetEnd;
	}

	public void setMeetEnd(String meetEnd) {
		this.meetEnd = meetEnd;
	}

	public String getMeetNum() {
		return meetNum;
	}

	public void setMeetNum(String meetNum) {
		this.meetNum = meetNum;
	}

	public String getMeetBranch() {
		return meetBranch;
	}

	public void setMeetBranch(String meetBranch) {
		this.meetBranch = meetBranch;
	}

	public String getMeetBuildingNum() {
		return meetBuildingNum;
	}

	public void setMeetBuildingNum(String meetBuildingNum) {
		this.meetBuildingNum = meetBuildingNum;
	}

	public String getMeetBuildingName() {
		return meetBuildingName;
	}

	public void setMeetBuildingName(String meetBuildingName) {
		this.meetBuildingName = meetBuildingName;
	}

	public String getMeetClassNum() {
		return meetClassNum;
	}

	public void setMeetClassNum(String meetClassNum) {
		this.meetClassNum = meetClassNum;
	}

	public String getMeetTeacher() {
		return meetTeacher;
	}

	public void setMeetTeacher(String meetTeacher) {
		this.meetTeacher = meetTeacher;
	}

	public String getMeetStatus() {
		return meetStatus;
	}

	public void setMeetStatus(String meetStatus) {
		this.meetStatus = meetStatus;
	}

	public String getMeetTopic() {
		return meetTopic;
	}

	public void setMeetTopic(String meetTopic) {
		this.meetTopic = meetTopic;
	}

	public String getMeetClassName() {
		return meetClassName;
	}

	public void setMeetClassName(String meetClassName) {
		this.meetClassName = meetClassName;
	}

	@Override
	public String toString() {
		return "{meetDate:" + meetDate + ", meetStart:" + meetStart
				+ ", meetEnd:" + meetEnd + ", meetNum:" + meetNum
				+ ", meetBranch:" + meetBranch + ", meetBuildingNum:"
				+ meetBuildingNum + ", meetBuildingName:" + meetBuildingName
				+ ", meetClassNum:" + meetClassNum + ", meetTeacher:"
				+ meetTeacher + ", meetStatus:" + meetStatus + ", meetTopic:"
				+ meetTopic + ", meetClassName:" + meetClassName + "}";
	}

}
