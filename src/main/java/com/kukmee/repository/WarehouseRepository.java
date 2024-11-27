package com.kukmee.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kukmee.entity.Warehouse;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
	
	Optional<Warehouse> findByUsername(String username);

	boolean existsByUsername(String username);
}
