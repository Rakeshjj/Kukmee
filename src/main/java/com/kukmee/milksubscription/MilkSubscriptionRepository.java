package com.kukmee.milksubscription;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MilkSubscriptionRepository extends JpaRepository<MilkSubscription, Long> {
}
