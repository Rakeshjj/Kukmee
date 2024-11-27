package com.kukmee.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kukmee.entity.Cook;

@Repository
public interface CookRepository extends JpaRepository<Cook, Long> {
	Optional<Cook> findByUsername(String username);

	boolean existsByUsername(String username);

	boolean existsByEmail(String email);

	// Change the method name to match the field name 'phonenumber'
	boolean existsByPhonenumber(Long phonenumber); // Make sure to use 'phonenumber' as field name
}
