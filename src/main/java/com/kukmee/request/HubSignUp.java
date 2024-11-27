package com.kukmee.request;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class HubSignUp {

	@NotBlank
	@Size(max = 50)
	@Pattern(regexp = "^[A-Za-z]*$", message = "Invalid Input: Hub name only accepts characters")
	private String username;

	@NotBlank
	@Size(max = 120)
	private String password;

	@NotBlank
	@Email(message = "Invalid email format")
	@Size(max = 50, message = "Email should be no more than 50 characters long")
	private String email;

	@NotNull
    @Column(unique = true)
	private Long phonenumber;

	@NotBlank
	private String hubName;

	@NotBlank
	private String location;

	@NotBlank
	private String managerName;

	private Set<String> role;

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

	public String getHubName() {
		return hubName;
	}

	public void setHubName(String hubName) {
		this.hubName = hubName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getManagerName() {
		return managerName;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

	public Set<String> getRole() {
		return role;
	}

	public void setRole(Set<String> role) {
		this.role = role;
	}

}
