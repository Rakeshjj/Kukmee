package com.kukmee.kukmart;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class DeliveryDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String deliveryAddress;

	@Column(nullable = false)
	private String deliveryDate;

	@Column(nullable = false)
	private String deliveryTimeSlot;

//	@Column(nullable = false)
//	private String paymentType; // PAY_ON_DELIVERY or PAY_NOW
}
