package com.onpost.global.fcm;

import lombok.Getter;

import java.util.List;

@Getter
public class NotificationEvent {

    private final String message;
    private final List<String> deviceTokens;

    public NotificationEvent(String message, List<String> deviceTokens) {
        this.message = message;
        this.deviceTokens = deviceTokens;
    }
}
