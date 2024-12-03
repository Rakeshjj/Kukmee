package com.kukmee.bookingentity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AmountDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private double totalAmount;
	private double advancePayment;
	private double balanceAmount;

	@OneToOne
	private BartenderBooking booking; // Link back to the BartenderBooking

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public double getAdvancePayment() {
		return advancePayment;
	}

	public void setAdvancePayment(double advancePayment) {
		this.advancePayment = advancePayment;
	}

	public double getBalanceAmount() {
		return balanceAmount;
	}

	public void setBalanceAmount(double balanceAmount) {
		this.balanceAmount = balanceAmount;
	}

	public BartenderBooking getBooking() {
		return booking;
	}

	public void setBooking(BartenderBooking booking) {
		this.booking = booking;
	}

	// Getters and setters
}
