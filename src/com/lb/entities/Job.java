package com.lb.entities;

public class Job {
	private String jobId;
	private String title;
	private String company;
	private String area;
	private String domain;
	private String experience;
	private String text;
	private String requirements;
	private String email;

	public Job() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Job(String jobId, String title, String company, String area,
			String domain, String experience, String text, String requirements,
			String email) {
		super();
		this.jobId = jobId;
		this.title = title;
		this.company = company;
		this.area = area;
		this.domain = domain;
		this.experience = experience;
		this.text = text;
		this.requirements = requirements;
		this.email = email;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getExperience() {
		return experience;
	}

	public void setExperience(String experience) {
		this.experience = experience;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getRequirements() {
		return requirements;
	}

	public void setRequirements(String requirements) {
		this.requirements = requirements;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "Job [jobId=" + jobId + ", title=" + title + ", company="
				+ company + ", area=" + area + ", domain=" + domain
				+ ", experience=" + experience + ", text=" + text
				+ ", requirements=" + requirements + ", email=" + email + "]";
	}

}
