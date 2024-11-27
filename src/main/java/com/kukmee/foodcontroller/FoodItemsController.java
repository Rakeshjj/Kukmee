package com.kukmee.foodcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kukmee.foodentity.FoodItem;
import com.kukmee.foodservice.FoodItemService;

@RestController
@RequestMapping("/api/fooditems")
public class FoodItemsController {

    @Autowired
    private FoodItemService foodItemService;

    // Allow only Admin to access this endpoint
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/save")
    public ResponseEntity<?> savefood(@RequestBody FoodItem foodItem) {
        foodItemService.save(foodItem);
        return ResponseEntity.ok("New food item inserted successfully.");
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/getAll")
    public ResponseEntity<List<FoodItem>> getAll() {
        List<FoodItem> fooditemall = foodItemService.getAll();
        return ResponseEntity.ok(fooditemall);
    }

    @GetMapping("/veg")
    public ResponseEntity<List<FoodItem>> getvegItems() {
        List<FoodItem> vegfood = foodItemService.getVegFoodItems();
        return ResponseEntity.ok(vegfood);
    }

    @GetMapping("/nonveg")
    public ResponseEntity<List<FoodItem>> getNonvegItems() {
        List<FoodItem> nonvegFood = foodItemService.getNonVegFoodItems();
        return ResponseEntity.ok(nonvegFood);
    }

    @PostMapping("/update/{foodId}")
    public ResponseEntity<?> updatefood(@RequestBody FoodItem foodItem, @PathVariable Long foodId) {
        foodItemService.update(foodItem, foodId);
        return ResponseEntity.ok("Updated successfully.");
    }

    @DeleteMapping("/delete/{foodid}")
    public ResponseEntity<?> deletefoodItems(@PathVariable Long foodid) {
        foodItemService.deleteById(foodid);
        return ResponseEntity.ok("Deleted successfully.");
    }

    @GetMapping("/getById/{foodid}")
    public ResponseEntity<FoodItem> getById(@PathVariable Long foodid) {
        FoodItem foodItem = foodItemService.getById(foodid);
        return new ResponseEntity<>(foodItem, HttpStatus.OK);
    }
}
