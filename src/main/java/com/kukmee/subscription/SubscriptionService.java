package com.kukmee.subscription;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kukmee.exception.ResourceNotFoundException;

@Service
public class SubscriptionService {

	@Autowired
	private SubscriptionRepository subscriptionRepository;

	@Autowired
	private PlanRepository planRepository;

	public Subscription createSubscription(String username, String planName, LocalDate startDate, LocalDate endDate) {

		Plan plan = planRepository.findByPlanName(planName);

		if (plan == null) {
			throw new RuntimeException("Plan not found: " + planName);
		}

		int selectedDuration = (int) (endDate.toEpochDay() - startDate.toEpochDay());
		if (selectedDuration + 1 > plan.getDurationInDays()) {
			throw new RuntimeException("Invalid subscription duration. The selected plan allows only "
					+ plan.getDurationInDays() + " days.");
		}

		Subscription subscription = new Subscription();
		subscription.setUsername(username);
		subscription.setPlan(plan);
		subscription.setStartDate(startDate);
		subscription.setEndDate(endDate);
		subscription.setIsActive(true);

		return subscriptionRepository.save(subscription);
	}

	public List<Subscription> getAll() {
		return subscriptionRepository.findAll();
	}

	public Subscription getById(Long id) {
		return subscriptionRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("ID NOT FOUND :" + id));
	}

	public void deleteById(Long id) {
		if (subscriptionRepository.existsById(id)) {
			throw new ResourceNotFoundException("ID NOT FOUND :" + id);
		}
		subscriptionRepository.deleteById(id);
	}

	public Subscription update(Subscription subscription, Long id) {
		subscriptionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("ID NOT FOUND :" + id));
		return null;
	}
}
