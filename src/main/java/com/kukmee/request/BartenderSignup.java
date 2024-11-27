package com.kukmee.request;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class BartenderSignup {

	@Size(max = 50)
	@NotBlank
	@Pattern(regexp = "^[A-Za-z]*$", message = "Invalid Input: Only accept charcters")
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

	private Long phonenumber;
//	private String profilepicture;

	@NotNull(message = "Years of experience cannot be null")
	@Min(value = 1, message = "Years of experience must be at least 1")
	@Max(value = 50, message = "Years of experience cannot exceed 50")
	private Integer bartenderExperience; // Years of experience

	@Size(max = 50)
	@NotBlank
	@Pattern(regexp = "^[A-Za-z]*$", message = "Invalid Input: Only accept charcters")
	private String drinkSpecialty; // E.g., Cocktails, Mixology

	@NotNull(message = "Roles cannot be null")
	private Set<String> role;

	public BartenderSignup(String username, String email, String password, Long phonenumber,
			Integer bartenderExperience, String drinkSpecialty,
			@NotNull(message = "Roles cannot be null") Set<String> role) {
		super();
		this.username = username;
		this.email = email;
		this.password = password;
		this.phonenumber = phonenumber;
		this.bartenderExperience = bartenderExperience;
		this.drinkSpecialty = drinkSpecialty;
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

	public Long getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(Long phonenumber) {
		this.phonenumber = phonenumber;
	}

	public Integer getBartenderExperience() {
		return bartenderExperience;
	}

	public void setBartenderExperience(Integer bartenderExperience) {
		this.bartenderExperience = bartenderExperience;
	}

	public String getDrinkSpecialty() {
		return drinkSpecialty;
	}

	public void setDrinkSpecialty(String drinkSpecialty) {
		this.drinkSpecialty = drinkSpecialty;
	}

	public Set<String> getRole() {
		return role;
	}

	public void setRole(Set<String> role) {
		this.role = role;
	}

}
