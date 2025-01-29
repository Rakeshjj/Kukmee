package com.kukmee.wishlist;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "product name cannot be null")
	private String name;

	private String image;

	@Embedded
	private Price price;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "product_id")
	private List<com.kukmee.wishlist.Size> sizes;

//	@ManyToOne
//    @JoinColumn(name = "customer_id", nullable = false)
//	private Customer customer;
}
