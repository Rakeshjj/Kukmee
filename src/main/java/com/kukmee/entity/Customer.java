package com.kukmee.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customers", uniqueConstraints = { @UniqueConstraint(columnNames = "username") })
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long customerid;

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

	@NotNull(message = "Phone number is required")
	private Long phonenumber;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "customer_roles", joinColumns = { @JoinColumn(name = "customer_id") }, inverseJoinColumns = {
			@JoinColumn(name = "role_id") })
	private Set<Role> roles = new HashSet<>();

	@Column(nullable = true)
	private String resetToken;

	@Column(nullable = true)
	private LocalDateTime resetTokenExpirationTime;

	@CreationTimestamp
	private LocalDateTime createdAt;

	@UpdateTimestamp
	private LocalDateTime updatedAt;

	private boolean isActive = true;

	// Add constructor for initialization
	public Customer(String username, String email, String password, Long phonenumber) {
		this.username = username;
		this.email = email;
		this.password = password;
		this.phonenumber = phonenumber;
	}
}
