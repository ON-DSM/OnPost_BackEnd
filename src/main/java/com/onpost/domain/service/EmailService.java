package com.onpost.domain.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.onpost.domain.dto.SenderDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Slf4j
@Service
public record EmailService(JavaMailSender javaMailSender, AmazonSimpleEmailService amazonSimpleEmailService) {

    public void sendMail(String toEmail, String subject, String message) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            helper.setFrom("OnPost");
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(message, true);

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendSESMail(SenderDto senderDto) {
        try {
            amazonSimpleEmailService.sendEmail(senderDto.toSendRequestDto());
        } catch (Exception e) {
            log.error("Error Message: " + e.getMessage());
            throw new AmazonClientException(e.getMessage(), e);
        }
    }
}
