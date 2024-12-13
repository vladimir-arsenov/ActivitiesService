package org.example.notificationservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailNotificationService {

    private final JavaMailSender javaMailSender;

    public void sendEmail(String to, String subject, String text) {
        try {
            log.debug("Sending email to {} with subject {}", to, subject);
            var message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            javaMailSender.send(message);
        } catch (Exception e) {
            log.warn("Failed to send email to {} with subject {}. Error: {}", to, subject, e.getMessage());
        }
    }

}
