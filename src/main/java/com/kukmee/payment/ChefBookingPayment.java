package com.kukmee.payment;

import com.kukmee.chef.ChefBooking;
import com.kukmee.chef.ChefBookingMultipleDays;
import com.kukmee.chef.ChefMonthlyBooking;
import com.kukmee.chefbookings.ChefServiceBooking;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ChefBookingPayment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "session_id", nullable = false, unique = true)
	private String sessionId;

	@Column(name = "payment_id")
	private String paymentId;

	private double amount;
	private String currency;
	private String status;

	@ManyToOne
	@JoinColumn(name = "chef_booking_id")
	private ChefServiceBooking chefServiceBooking;

}
