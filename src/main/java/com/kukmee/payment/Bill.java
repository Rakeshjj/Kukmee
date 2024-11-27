package com.kukmee.payment;

import java.util.Date;

import com.kukmee.orders.Order;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Bill {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long billId;

	@OneToOne(cascade = CascadeType.ALL)
	private Order order;

	private String paymentId;
	private Double totalAmount;
	private String currency;
	private String status;
	private Date paymentDate;
}
