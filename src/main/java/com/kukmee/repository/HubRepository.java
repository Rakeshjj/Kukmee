package com.kukmee.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kukmee.entity.Hub;

public interface HubRepository extends JpaRepository<Hub, Long> {

	Optional<Hub> findByUsername(String username);

	boolean existsByUsername(String username);
}
