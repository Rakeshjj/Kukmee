package com.kukmee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kukmee.entity.Chef;

@Repository
public interface ChefRepository extends JpaRepository<Chef, Long> {


	boolean existsByUsername(String username);

	boolean existsByEmail(String email);

	boolean existsByPhonenumber(Long phonenumber); 
}
