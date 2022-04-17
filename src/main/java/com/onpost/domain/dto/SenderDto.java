package com.onpost.domain.dto;

import com.amazonaws.services.simpleemail.model.*;
import lombok.Builder;

public record SenderDto(String from, String to, String subject, String content) {

    @Builder
    public SenderDto {
    }

    public SendEmailRequest toSendRequestDto() {
        Destination destination = new Destination()
                .withToAddresses(this.to);

        Message message = new Message()
                .withSubject(createContent(this.subject))
                .withBody(new Body()
                        .withHtml(createContent(this.content)));

        return new SendEmailRequest()
                .withDestination(destination)
                .withMessage(message)
                .withSource(this.from);
    }

    private Content createContent(String text) {
        return new Content()
                .withCharset("UTF-8")
                .withData(text);
    }
}
