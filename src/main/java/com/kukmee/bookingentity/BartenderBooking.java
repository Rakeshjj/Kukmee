package com.kukmee.bookingentity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class BartenderBooking {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long bookingid;
	private String occasion;
	private LocalDate bookingDate;
	private LocalTime bartenderArrivalTime;
	private int numberOfGuests;

	@OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<Drink> selectedDrinks;

	@OneToOne(cascade = CascadeType.ALL)
	private AmountDetails amountDetails;

	// Getters and setters
}
