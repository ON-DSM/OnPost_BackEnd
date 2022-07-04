package com.onpost.domain.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import com.onpost.domain.dto.ChatMessageDto;
import com.onpost.domain.entity.Category;
import com.onpost.global.error.ErrorCode;
import com.onpost.global.error.exception.MasterException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedDeque;

@Slf4j
@Service
public class ChatDomainService {

    public static final Queue<SocketIOClient> matchingMentorQueue = new ConcurrentLinkedDeque<>();
    public static final Queue<SocketIOClient> matchingMenteeQueue = new ConcurrentLinkedDeque<>();

    private final SocketIONamespace namespace;

    @Autowired
    public ChatDomainService(SocketIOServer server) {
        namespace = server.addNamespace("/chat");
        namespace.addEventListener("send", ChatMessageDto.class, onSend());
        namespace.addEventListener("stop", Void.class, onStop());
        namespace.addEventListener("match", String.class, onMatching());
        namespace.addEventListener("exit", String.class, onExit());
    }

    private DataListener<String> onMatching() {
        return (client, data, ackSender) -> {
            Category type = Category.valueOf(data);

            if (type == null) throw new MasterException(ErrorCode.PARAMETER_NOT_CONTAIN);

            log.info("Join : {}", client.getSessionId());

            client.set("type", type);
            switch (type) {
                case MENTEE -> {
                    if (!matchingMentorQueue.isEmpty()) makeRoom(client, matchingMentorQueue.poll());
                }
                case MENTOR -> {
                    if (!matchingMenteeQueue.isEmpty()) makeRoom(client, matchingMenteeQueue.poll());
                }
                default -> throw new MasterException(ErrorCode.PARAMETER_NOT_CONTAIN);
            }
        };
    }

    private DataListener<Void> onStop() {
        return (client, data, ackSender) -> {
            log.info("Stop : {}", client.getSessionId());
            switch ((Category) client.get("type")) {
                case MENTEE -> matchingMenteeQueue.remove(client);
                case MENTOR -> matchingMentorQueue.remove(client);
                default -> throw new MasterException(ErrorCode.PARAMETER_NOT_CONTAIN);
            }
        };
    }

    private DataListener<ChatMessageDto> onSend() {
        return (client, data, ackSender) -> {
            log.info("message : {}, sessionId : {}", data.getMessage(), client.getSessionId());
            namespace.getRoomOperations(data.getRoomId()).sendEvent("");
        };
    }

    private DataListener<String> onExit() {
        return (client, data, ackSender) -> {
            log.info("Exit : {}", client.getSessionId());
            client.leaveRoom(data);
            namespace.getRoomOperations(data).sendEvent("exitUser");
        };
    }

    private void makeRoom(SocketIOClient user1, SocketIOClient user2) {
        UUID uuid = UUID.randomUUID();

        user1.joinRoom(uuid.toString());
        user2.joinRoom(uuid.toString());

        user1.sendEvent("connected", (Category) user1.get("type"), uuid.toString());
        user2.sendEvent("connected", (Category) user1.get("type"), uuid.toString());
    }

}
