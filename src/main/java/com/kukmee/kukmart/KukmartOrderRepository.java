package com.kukmee.kukmart;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kukmee.entity.Customer;

public interface KukmartOrderRepository extends JpaRepository<KukmartOrder, Long> {
	List<KukmartOrder> findByCustomer(Customer customer);
}
