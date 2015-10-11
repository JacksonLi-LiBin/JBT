package com.lb.entities;

import java.io.Serializable;

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
	private String phone;
	private String email;
	private String organization;
	private String dataAndTimeOfLead;
	private String campaign;
	private String leadSource;
	private String studyExtension;
	private String agreeForad;
	private String interest;
	private int userId;
	private int emailStatus;

	public Friend() {
		super();
	}

	public Friend(String firstName, String lastName, String phone, String email, String organization,
			String dataAndTimeOfLead, String campaign, String leadSource, String studyExtension, String agreeForad,
			String interest, int userId, int emailStatus) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.email = email;
		this.organization = organization;
		this.dataAndTimeOfLead = dataAndTimeOfLead;
		this.campaign = campaign;
		this.leadSource = leadSource;
		this.studyExtension = studyExtension;
		this.agreeForad = agreeForad;
		this.interest = interest;
		this.userId = userId;
		this.emailStatus = emailStatus;
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

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getEmailStatus() {
		return emailStatus;
	}

	public void setEmailStatus(int emailStatus) {
		this.emailStatus = emailStatus;
	}

	@Override
	public String toString() {
		return "Friend [firstName=" + firstName + ", lastName=" + lastName + ", phone=" + phone + ", email=" + email
				+ ", organization=" + organization + ", dataAndTimeOfLead=" + dataAndTimeOfLead + ", campaign="
				+ campaign + ", leadSource=" + leadSource + ", studyExtension=" + studyExtension + ", agreeForad="
				+ agreeForad + ", interest=" + interest + ", userId=" + userId + ", emailStatus=" + emailStatus + "]";
	}

}
