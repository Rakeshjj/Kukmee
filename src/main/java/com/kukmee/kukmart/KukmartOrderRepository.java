package com.kukmee.kukmart;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface KukmartOrderRepository extends JpaRepository<KukmartOrder, Long> {
    List<KukmartOrder> findByCustomerUsername(String customerUsername);

//	Page<KukmartOrder> findByCustomerUsernamePage(String customerUsername, Pageable pageable);
}

