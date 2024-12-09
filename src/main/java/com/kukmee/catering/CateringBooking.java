package com.kukmee.catering;

import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
public class CateringBooking {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long cateringId;

	@NotNull(message = "occasion cannot be null")
	@Pattern(regexp = "^[^\\d].*", message = "Occasion cannot start with a number")
	private String occasion;

	@NotNull(message = "EventDate cannot be null")
	@Pattern(regexp = "^([0-2][0-9]|3[01])-(0[1-9]|1[0-2])-\\d{4}$", message = "Date must be in the format DD-MM-YYYY")
	private String eventDate;

	@NotNull(message = "EventTime cannot be null")
	@Pattern(regexp = "^(0?[1-9]|1[0-2]):([0-5][0-9])\\s([APap][Mm])$", message = "Event time must be in 12-hour format (e.g., 6:30 PM).")
	private String eventTime;

	@Min(value = 1, message = "Number of people must be at least 1.")
	@Max(value = 300, message = "Number of people cannot exceed 300.")
	private int numberOfPeople;

	@NotNull(message = "Cuisine cannot be null")
	@Pattern(regexp = "^[^\\d].*", message = "Cuisine cannot start with a number")
	private String cuisine;

	@NotNull(message = "Phone number is required")
	private Long phoneNumber;

	@NotNull(message = "Location cannot be null")
	@Pattern(regexp = "^[^\\d].*", message = "Location cannot start with a number")
	private String venueLocation;

	@ElementCollection
	@CollectionTable(name = "catering_selected_menu")
	private List<CateringMenuItem> selectedMenu;

	private double serviceCharge;
	private double menuCharge;
	private double gst;
	private double totalAmount;
}
