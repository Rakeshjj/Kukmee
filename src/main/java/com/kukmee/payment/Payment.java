package com.kukmee.payment;

import com.kukmee.catering.CateringBooking;
import com.kukmee.chef.ChefBooking;
import com.kukmee.orders.Order;

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
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Payment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String sessionId;
	private double amount;
	private Long quantity;
	private String currency;
	private String status;

	@ManyToOne
	@JoinColumn(name = "order_id") // This column will be the foreign key in the Payment table
	private Order order; // The associated Order entity

	@ManyToOne
	@JoinColumn(name = "chef_booking_id") // Foreign key for ChefBooking
	private ChefBooking chefBooking;

	@ManyToOne
	@JoinColumn(name = "catering_booking_id")
	private CateringBooking cateringBooking;
}
