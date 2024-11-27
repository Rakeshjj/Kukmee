package com.kukmee.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kukmee.entity.SubAdmin;

public interface SubAdminRepository extends JpaRepository<SubAdmin, Long>{

	Optional<SubAdmin> findByUsername(String username);

	boolean existsByUsername(String username);
}
