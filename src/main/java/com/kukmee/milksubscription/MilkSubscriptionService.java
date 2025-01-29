package com.kukmee.milksubscription;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.kukmee.entity.Customer;
import com.kukmee.repository.CustomerRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class MilkSubscriptionService {

    private static final Logger logger = LoggerFactory.getLogger(MilkSubscriptionService.class);

    @Autowired
    private MilkSubscriptionRepository milkSubscriptionRepository;

    @Autowired
    private SubscriptionPauseRepository subscriptionPauseRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private CustomerRepository customerRepository;

    public MilkSubscription createSubscription(Long customerid, MilkSubscription subscription) {

        Customer customer = customerRepository.findById(customerid)
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + customerid));

        subscription.setCustomer(customer);
        return milkSubscriptionRepository.save(subscription);
    }

    public SubscriptionPause pauseSubscription(Long subscriptionId, LocalDate pauseDate) {
        Optional<MilkSubscription> subscriptionOpt = milkSubscriptionRepository.findById(subscriptionId);
        if (subscriptionOpt.isEmpty()) {
            throw new RuntimeException("Subscription not found.");
        }

        MilkSubscription subscription = subscriptionOpt.get();
        SubscriptionPause pause = new SubscriptionPause();
        pause.setPauseDate(pauseDate);
        pause.setMilkSubscription(subscription);

        subscriptionPauseRepository.save(pause);

        try {
            // Send notification email to admin
            sendPauseNotification(subscription.getCustomer().getCustomerid(), pauseDate);
        } catch (MessagingException e) {
            logger.error("Failed to send pause notification email: {}", e.getMessage());
            throw new RuntimeException("Failed to send pause notification email", e);
        }

        return pause;
    }

    private void sendPauseNotification(Long userId, LocalDate pauseDate) throws MessagingException {
        String adminEmail = "admin@kukmee.com"; // Change this to your admin email

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(adminEmail);
        helper.setSubject("Milk Subscription Paused");
        helper.setText("User ID: " + userId + " has paused their milk subscription on: " + pauseDate);

        mailSender.send(message);
        logger.info("Pause notification sent to admin for user ID: {} on date: {}", userId, pauseDate);
    }

    public List<MilkSubscription> getAllSubscriptions() {
        return milkSubscriptionRepository.findAll();
    }
}
