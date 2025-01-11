package com.kukmee.Mailsender;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.kukmee.subscription.SubscriptionPlan;
import com.kukmee.subscription.SubscriptionPlanRepository;

@Service
public class NotificationService {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private SubscriptionPlanRepository subscriptionPlanRepository;

	@Scheduled(cron = "0 40 11 * * ?")
	public void sendDailyNotification() {

		List<SubscriptionPlan> activeSubscriptions = getActiveSubscriptions();

		for (SubscriptionPlan subscriptionPlan : activeSubscriptions) {

			if (subscriptionPlan.getCustomer() == null) {
				System.out.println("Subscription ID: " + subscriptionPlan.getId() + " has no customer associated.");
			} else if (subscriptionPlan.getCustomer().getEmail() == null) {
				System.out.println("Customer for Subscription ID: " + subscriptionPlan.getId() + " has no email.");
			} else {
				String customerEmail = subscriptionPlan.getCustomer().getEmail();

				String subject = "Your Daily Meal notification";
				String message = "Your food is ready for today. Enjoy your meal.";
				try {
					sendSimpleMessage(customerEmail, subject, message);
					System.out.println("Email successfully sent to:" + customerEmail);
				} catch (Exception e) {
					System.out.println("Failed to send email to:" + customerEmail);
					e.printStackTrace();
				}

			}
		}
	}

	public void notifyExpiringSubscriptions() {
		List<SubscriptionPlan> expiringPlan = subscriptionPlanRepository.findExpiringSoonSubscriptions();

		for (SubscriptionPlan subscriptionPlan : expiringPlan) {
			String customerEmail = subscriptionPlan.getCustomer().getEmail();
			String subject = "Subscription expiry Notice";
			String message = "Dear " + subscriptionPlan.getCustomer().getUsername() + ",\n\nYour subscription for "
					+ subscriptionPlan.getPlanType() + " will expire tomorrow. Renew now to avoid interruption!";
			sendSimpleMessage(customerEmail, subject, message);
		}
	}  
	
	@Scheduled(cron = "0 0 8 * * ?") // Runs daily at 8:00 AM
	public void checkExpiredSubscriptions() {
		List<SubscriptionPlan> activeSubscriptions = subscriptionPlanRepository.findActiveSubscriptions();

		for (SubscriptionPlan subscription : activeSubscriptions) {
			
			LocalDate calculatedEndDate = subscription.getStartDate().plusDays(subscription.getDuration());
			
			if (calculatedEndDate.isBefore(LocalDate.now()) || calculatedEndDate.isEqual(LocalDate.now())) {
				subscription.setExpired(true);
				subscriptionPlanRepository.save(subscription);

				String email = subscription.getCustomer().getEmail();
				String message = "Dear " + subscription.getCustomer().getUsername() + ",\n\nYour subscription for "
						+ subscription.getPlanType() + " expired on " + calculatedEndDate
						+ ". Renew now to continue enjoying our service.";
				sendSimpleMessage(email, "Subscription Expired", message);
			}
		}
	}

	public void sendSimpleMessage(String to, String subject, String text) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("rakeshdurai1711@gmail.com");
		message.setTo(to);
		message.setSubject(subject);
		message.setText(text);
		mailSender.send(message);
	}

	public List<SubscriptionPlan> getActiveSubscriptions() {
		return subscriptionPlanRepository.findByAvailability("Daily");
	}
}
