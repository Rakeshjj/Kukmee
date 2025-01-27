package com.kukmee.subscription;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, Long> {

//	@Query("SELECT sp FROM SubscriptionPlan sp JOIN FETCH sp.customer WHERE sp.planType = :planType")
//	List<SubscriptionPlan> findActiveSubscriptions(@Param("planType") String planType);
//	
//    List<SubscriptionPlan> findByAvailability(String availability);
//    
//    @Query(value = "SELECT sp FROM SubscriptionPlan sp WHERE sp.isExpired = false AND DATE_ADD(sp.subscriptionStartDate, INTERVAL sp.duration DAY) <= CURRENT_DATE", nativeQuery = true)
//    List<SubscriptionPlan> findExpiringSoonSubscriptions();
//
//    
//    @Query("SELECT sp FROM SubscriptionPlan sp WHERE sp.isExpired = false")
//    List<SubscriptionPlan> findActiveSubscriptions();

}
