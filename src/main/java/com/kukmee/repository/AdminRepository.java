package com.kukmee.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kukmee.entity.Admin;
import com.kukmee.entity.Role;

public interface AdminRepository extends JpaRepository<Admin, Long> {

	Optional<Admin> findByUsername(String username);

	boolean existsByUsername(String username);

	boolean existsByRolesContaining(Role role);
}
