package com.onpost.global.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@ConfigurationProperties("socketio")
@ConstructorBinding
public class SocketIoProperties {

    private final String host;
    private final Integer port;

    public SocketIoProperties(String host, Integer port) {
        this.host = host;
        this.port = port;
    }
}
