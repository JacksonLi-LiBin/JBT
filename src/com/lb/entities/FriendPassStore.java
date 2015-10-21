package com.lb.entities;

import java.io.Serializable;

public class FriendPassStore implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1928709621913105787L;
	private int friendId;
	private String firstName;
	private String lastName;
	private String phone;
	private String email;
	private String organization;
	private String dataAndTimeOfLead;
	private String campaign;
	private String leadSource;
	private String studyExtension;
	private String agreeForad;
	private String interest;
	private String userId;
	private String emailStatus;

	public FriendPassStore() {
		super();
		organization = "jbt";
		campaign = "448";
		leadSource = "894";
		studyExtension = "1";
		agreeForad = "0";
		emailStatus = "0";
	}

	public int getFriendId() {
		return friendId;
	}

	public void setFriendId(int friendId) {
		this.friendId = friendId;
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

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getDataAndTimeOfLead() {
		return dataAndTimeOfLead;
	}

	public void setDataAndTimeOfLead(String dataAndTimeOfLead) {
		this.dataAndTimeOfLead = dataAndTimeOfLead;
	}

	public String getCampaign() {
		return campaign;
	}

	public void setCampaign(String campaign) {
		this.campaign = campaign;
	}

	public String getLeadSource() {
		return leadSource;
	}

	public void setLeadSource(String leadSource) {
		this.leadSource = leadSource;
	}

	public String getStudyExtension() {
		return studyExtension;
	}

	public void setStudyExtension(String studyExtension) {
		this.studyExtension = studyExtension;
	}

	public String getAgreeForad() {
		return agreeForad;
	}

	public void setAgreeForad(String agreeForad) {
		this.agreeForad = agreeForad;
	}

	public String getInterest() {
		return interest;
	}

	public void setInterest(String interest) {
		this.interest = interest;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getEmailStatus() {
		return emailStatus;
	}

	public void setEmailStatus(String emailStatus) {
		this.emailStatus = emailStatus;
	}

	@Override
	public String toString() {
		return "{friendId:" + friendId + ",firstName:" + firstName
				+ ", lastName:" + lastName + ", phone:" + phone + ", email:"
				+ email + ", organization:" + organization
				+ ", dataAndTimeOfLead:" + dataAndTimeOfLead + ", campaign:"
				+ campaign + ", leadSource:" + leadSource + ", studyExtension:"
				+ studyExtension + ", agreeForad:" + agreeForad + ", interest:"
				+ interest + ", userId:" + userId + ", emailStatus:"
				+ emailStatus + "}";
	}

}
