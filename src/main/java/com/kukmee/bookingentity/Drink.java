package com.kukmee.bookingentity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Drink {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long drinkid;
	private String type; // cocktail, mocktail
	private String name;
	private double price;

	@ManyToOne
	@JoinColumn(name = "booking_id")
	@JsonBackReference
	private BartenderBooking booking; // This is the missing property to map the relationship

	// Getters and setters
	public BartenderBooking getBooking() {
		return booking;
	}

	public void setBooking(BartenderBooking booking) {
		this.booking = booking;
	}

	public Long getDrinkid() {
		return drinkid;
	}

	public void setDrinkid(Long drinkid) {
		this.drinkid = drinkid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

}
