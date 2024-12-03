package com.kukmee.catering;

import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
	private Long id;

	private String occasion;
	
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Date must be in the format YYYY-MM-DD")
	private String eventDate;
	private String eventTime;
	private int numberOfPeople;
	private String cuisine;
	private String venueLocation;

	@ElementCollection
	@CollectionTable(name = "catering_selected_menu")
	private List<CateringMenuItem> selectedMenu; // Use the renamed class

	private double serviceCharge;
	private double menuCharge;
	private double gst;
	private double totalAmount;
}
