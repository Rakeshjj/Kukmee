package com.kukmee.chef;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ChefMonthlyBooking {

	@Id
	private String id;

	@Min(value = 1, message = "Number of people must be at least 1.")
	@Max(value = 20, message = "Number of people cannot exceed 20.")
	private int numberOfPeople;

	@NotNull(message = "Start date cannot be null")
	@Pattern(regexp = "^([0-2][0-9]|3[01])-(0[1-9]|1[0-2])-\\d{4}$", message = "Date must be in the format DD-MM-YYYY")
	private String startDate;

	@NotNull(message = "Meal type cannot be null")
	private String mealType;

	@NotNull(message = "House number cannot be null")
	private String houseNumber;

	@NotNull(message = "Street area cannot be null")
	private String streetArea;

	@NotNull(message = "City cannot be null")
	@Pattern(regexp = "^[^\\d].*", message = "City cannot start with a number")
	private String city;
	
	private double basePrice;
	private double gst;
	private double totalAmount;

	// Add default constructor and other required methods.
}
