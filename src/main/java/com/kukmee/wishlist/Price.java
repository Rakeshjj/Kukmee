package com.kukmee.wishlist;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Price {

	@NotNull(message = "original cannot be null")
	@NotBlank
	private Double original;
	
	@NotBlank
	private Double discounted;
}
