package com.kukmee.franchise;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(name = "unique_phone", columnNames = "phone"),
		@UniqueConstraint(name = "unique_email", columnNames = "email") })
public class FranchiseInquiry {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Size(max = 50)
	@NotBlank
	@Pattern(regexp = "^[A-Za-z][A-Za-z0-9]*$", message = "Username cannot start with a number")
	private String name;

	@NotBlank
	@Size(max = 50)
	@Email
	@Column(nullable = false)
	@NotNull(message = "Email cannot be null")
	@Pattern(regexp = "^[^\\d].*", message = "Email cannot start with a number")
	private String email;

	@NotNull(message = "Phone number is required")
	private Long phone;

	@NotNull(message = "City cannot be null")
	@Pattern(regexp = "^[^\\d].*", message = "City cannot start with a number")
	private String city;

	@NotNull(message = "Message cannot be null")
	@Pattern(regexp = "^[^\\d].*", message = "Message cannot start with a number")
	private String message;

	@NotNull(message = "State cannot be null")
	@Pattern(regexp = "^[^\\d].*", message = "State cannot start with a number")
	private String state;

}
