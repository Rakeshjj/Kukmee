package com.kukmee.milksubscription;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kukmee.entity.Customer;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class MilkSubscription {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private int litersPerDay;
	private LocalDate startDate;
	private LocalDate endDate;
	private double totalPrice;

	@ManyToOne
	@JoinColumn(name = "customer_id", nullable = false)
	private Customer customer;
	
	@OneToMany(mappedBy = "milkSubscription", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	private List<SubscriptionPause> pauses;
}
