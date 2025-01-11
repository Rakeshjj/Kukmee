package com.kukmee.cateringbooking;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class EventBooking {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "fullName cannot be null")
	@Pattern(regexp = "^[A-Za-z][A-Za-z0-9]*$", message = "Name cannot start with a number")
	private String fullName;

	@NotNull(message = "Email cannot be null")
	@Email
	private String email;

	@NotNull(message = "Phone number is required")
	private Long phoneNumber;
	
	@NotNull(message = "Package cannot be null")
	private String packageName;
	
	@NotNull(message = "EventType cannot be null")
	private String eventType;
	
	@NotNull(message = "EventDate cannot be null")
	private String eventDate;
	
	@NotNull(message = "EventLocation cannot be null")
	private String eventLocation;

}
