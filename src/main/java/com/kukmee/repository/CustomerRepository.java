package com.kukmee.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kukmee.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
	Optional<Customer> findByUsername(String username);

	boolean existsByUsername(String username);
}
