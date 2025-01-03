package com.kukmee.kukmart;


import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface KukmartOrderRepository extends JpaRepository<KukmartOrder, Long> {
    List<KukmartOrder> findByCustomerUsername(String customerUsername);
}

