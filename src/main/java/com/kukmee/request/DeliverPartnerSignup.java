package com.kukmee.request;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class DeliverPartnerSignup {

	@Size(max = 50)
	@NotBlank
	@Pattern(regexp = "^[A-Za-z][A-Za-z0-9]*$", message = "Username cannot start with a number")
	private String username;

	@NotBlank
	@Size(max = 50)
	@Email
	@Column(nullable = false)
	private String email;

	@Size(max = 120)
	@NotBlank
	@Column(nullable = false)
	private String password;

	private String phonenumber;

	@NotNull(message = "Vehicle type is required")
	private String vehicletype;

	@NotNull(message = "Vehicle number is required")
	@Pattern(regexp = "^[A-Z]{2}\\s[0-9]{2}\\s[A-Z]{2}\\s[0-9]{4}$", message = "Invalid vehicle number format (e.g., KA 01 AB 1234)")
	private String vehiclenumber;

	@NotNull(message = "License number is required")
	@Size(min = 10, max = 15, message = "License number must be between 10 and 15 characters")
	private String licensenumber;

	@NotNull(message = "Roles cannot be null")
	private Set<String> role;

	public DeliverPartnerSignup(String username, String email, String password, String phonenumber, String vehicletype,
			String vehiclenumber, String licensenumber, Set<String> role) {
		this.username = username;
		this.email = email;
		this.password = password;
		this.phonenumber = phonenumber;
		this.vehicletype = vehicletype;
		this.vehiclenumber = vehiclenumber;
		this.licensenumber = licensenumber;
		this.role = role;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	public String getVehicletype() {
		return vehicletype;
	}

	public void setVehicletype(String vehicletype) {
		this.vehicletype = vehicletype;
	}

	public String getVehiclenumber() {
		return vehiclenumber;
	}

	public void setVehiclenumber(String vehiclenumber) {
		this.vehiclenumber = vehiclenumber;
	}

	public String getLicensenumber() {
		return licensenumber;
	}

	public void setLicensenumber(String licensenumber) {
		this.licensenumber = licensenumber;
	}

	public Set<String> getRole() {
		return role;
	}

	public void setRole(Set<String> role) {
		this.role = role;
	}

}
