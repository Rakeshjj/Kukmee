package com.kukmee.catering;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable // Mark this as an embeddable type
public class CateringMenuItem { // Use a unique name

	private String category;
	private String name;
}
