package com.onpost.global.fcm;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Arrays;

@Service
@Slf4j
@RequiredArgsConstructor
public class FcmService {

    @Value("${firebase.path}")
    private String path;

    private static final String[] SCOPES = { "https://www.googleapis.com/auth/firebase.messaging" };

    @PostConstruct
    private void initialize() {
        try {
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(new ClassPathResource(path).getInputStream())
                            .createScoped(Arrays.asList(SCOPES)))
                    .build();

            if(FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                log.info("Firebase application has been initialized");
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    @EventListener
    public void SendNotification(NotificationEvent event) {
        log.info("FCM SEND : " + event.getMessage());
        MulticastMessage message = MulticastMessage.builder()
                .putData("message", event.getMessage())
                .putData("time", "2:45")
                .addAllTokens(event.getDeviceTokens())
                .setApnsConfig(
                        ApnsConfig.builder()
                                .setAps(Aps.builder()
                                        .setSound("default")
                                        .build())
                                .build()
                ).build();
        FirebaseMessaging.getInstance().sendMulticastAsync(message);
    }
}
