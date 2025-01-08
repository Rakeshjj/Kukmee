package com.kukmee.dietarymeals;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/meal")
public class MealSubscriptionController {

	@Autowired
	private MealService mealService;

	@PreAuthorize("hasRole('CUSTOMER')")
	@GetMapping("/options")
	public ResponseEntity<List<MealSubscription>> getMealOptions() {

		List<MealSubscription> options = mealService.getMealOption();
		return ResponseEntity.ok(options);
	}

	@PreAuthorize("hasRole('CUSTOMER')")
	@PostMapping("/order")
	public ResponseEntity<MealSubscription> placeOrder(@RequestBody MealSubscription mealSubscription) {
		MealSubscription savedMealSubscription = mealService.createSubscription(mealSubscription);
		return ResponseEntity.ok(savedMealSubscription);
	}

	@GetMapping("/get/{id}")
	public ResponseEntity<MealSubscription> getById(@PathVariable Long id) {
		MealSubscription mealSubscription = mealService.getById(id);
		return ResponseEntity.ok(mealSubscription);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteById(@PathVariable Long id) {
		mealService.deleteById(id);
		return ResponseEntity.ok("Deleted successfully");

	}

}
