package com.onpost.global.fcm;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class NotificationEvent {

    private String message;
    private List<String> deviceTokens;

    public NotificationEvent(String message, List<String> deviceTokens) {
        this.message = message;
        this.deviceTokens = deviceTokens;
    }
}
