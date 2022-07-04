package com.onpost.domain.service;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {

    private final SocketIOServer socketIOServer;
    private final ConnectListener connectListener = client -> {
        log.info("Connect : {}", client.getSessionId());
    };
    private final DisconnectListener disconnectListener = client -> {
        log.info("Disconnect : {}", client.getSessionId());
        client.disconnect();
    };

    @PostConstruct
    private void start() {
        socketIOServer.addConnectListener(connectListener);
        socketIOServer.addDisconnectListener(disconnectListener);
        socketIOServer.start();
    }

    @PreDestroy
    private void stop() {
        socketIOServer.stop();
    }
}
