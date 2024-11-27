package com.kukmee.entity;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "hub", uniqueConstraints = { @UniqueConstraint(columnNames = "username"),
		@UniqueConstraint(columnNames = "phonenumber"), @UniqueConstraint(columnNames = "email") })
public class Hub {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Size(max = 50)
	@Pattern(regexp = "^[A-Za-z]*$", message = "Invalid Input: Hub name only accepts characters")
	private String username;

	@NotBlank
	@Size(max = 120)
	private String password;

	@NotBlank
	@Size(max = 50, message = "Email should be no more than 50 characters long")
	@Email(message = "Invalid email format")
	@Column(nullable = false)
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

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "hub_roles", joinColumns = { @JoinColumn(name = "hub_id") }, inverseJoinColumns = {
			@JoinColumn(name = "role_id") })
	private Set<Role> roles;

	private boolean isActive = true;

	public Hub(String username, String password, String email, Long phonenumber, String hubName, String location,
			String managerName) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.phonenumber = phonenumber;
		this.hubName = hubName;
		this.location = location;
		this.managerName = managerName;
	}

}
