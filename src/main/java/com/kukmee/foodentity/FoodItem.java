package com.kukmee.foodentity;

import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "food_item", uniqueConstraints = { @UniqueConstraint(columnNames = "foodname") })
public class FoodItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long foodid;

	@Size(max = 50)
	@NotBlank
	@Pattern(regexp = "^[A-Za-z]*$", message = "Invalid Input: Product name only accept charcters")
	private String foodname;

	@Size(max = 50)
	@NotBlank
	@Pattern(regexp = "^[A-Za-z ]*$", message = "Invalid Input: Description only accepts characters and spaces")
	private String description;

	@NotNull(message = "Price must be a number")
	@Positive(message = "Price must be greater than 0")
	private Double price;

	@Enumerated(EnumType.STRING)
	private FoodCategory category; // lunch, dinner

	@Enumerated(EnumType.STRING)
	private FoodType foodtype; // veg or nonveg

	@CreationTimestamp
	private LocalDateTime creatDateTime;

	@UpdateTimestamp
	private LocalDateTime updateDateTime;

}
