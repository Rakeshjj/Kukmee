package com.kukmee.request;

import java.util.Set;

public class ChefSignup {

	private String username;
	private String password;
	private String email;
	private Long phonenumber;
	private Integer yearsOfExperience;
	private String specialty; // E.g., Italian, French, Vegan

	
	private Set<String> role;

	public ChefSignup(String username, String password, String email, Long phonenumber, Integer yearsOfExperience,
			String specialty, Set<String> role) {
		super();
		this.username = username;
		this.password = password;
		this.email = email;
		this.phonenumber = phonenumber;
		this.yearsOfExperience = yearsOfExperience;
		this.specialty = specialty;
		this.role = role;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(Long phonenumber) {
		this.phonenumber = phonenumber;
	}

	public Integer getYearsOfExperience() {
		return yearsOfExperience;
	}

	public void setYearsOfExperience(Integer yearsOfExperience) {
		this.yearsOfExperience = yearsOfExperience;
	}

	public String getSpecialty() {
		return specialty;
	}

	public void setSpecialty(String specialty) {
		this.specialty = specialty;
	}

	public Set<String> getRole() {
		return role;
	}

	public void setRole(Set<String> role) {
		this.role = role;
	}

}
