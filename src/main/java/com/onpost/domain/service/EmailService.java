package com.onpost.domain.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.SendTemplatedEmailRequest;
import com.onpost.domain.dto.SenderDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public record EmailService(AmazonSimpleEmailService amazonSimpleEmailService) {

    public void sendMail(SendTemplatedEmailRequest request) {
        try {
            amazonSimpleEmailService.sendTemplatedEmail(request);
        } catch (Exception e) {
            log.error("Error Message: " + e.getMessage());
            throw new AmazonClientException(e.getMessage(), e);
        }
    }
}
