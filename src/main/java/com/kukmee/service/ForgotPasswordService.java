package com.kukmee.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kukmee.entity.Customer;
import com.kukmee.repository.CustomerRepository;

@Service
public class ForgotPasswordService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender javaMailSender;

    public void generatePasswordResetToken(String email) {
        Customer customer = customerRepository.findByEmail(email);

        if (customer == null) {
            throw new RuntimeException("User with email " + email + " not found");
        }

        String resetToken = UUID.randomUUID().toString();
        LocalDateTime expirationTime = LocalDateTime.now().plusHours(1);

        customer.setResetToken(resetToken);
        customer.setResetTokenExpirationTime(expirationTime);
        customerRepository.save(customer);

        sendResetPasswordEmail(email, resetToken);
    }

    public void resetPassword(String token, String newPassword) {
        Customer customer = customerRepository.findByResetToken(token);

        if (customer == null) {
            throw new RuntimeException("Invalid reset token");
        }

        if (customer.getResetTokenExpirationTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Reset token has expired");
        }

        customer.setPassword(passwordEncoder.encode(newPassword));
        customer.setResetToken(null);
        customer.setResetTokenExpirationTime(null);
        customerRepository.save(customer);
    }

    public void sendResetPasswordEmail(String email, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Password Reset Request");
        message.setText("To reset your password, click the following link: "
                + "https://rsz44sdt-5500.inc1.devtunnels.ms/reset-password.html?token=" + token); // Update this to your frontend URL
        javaMailSender.send(message);
    }

}
