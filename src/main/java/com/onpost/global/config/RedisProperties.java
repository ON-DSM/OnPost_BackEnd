package com.onpost.global.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@ConstructorBinding
@ConfigurationProperties("redis")
public class RedisProperties {

    private final Integer port;
    private final String host;
    private final String password;

    public RedisProperties(Integer port, String host, String password) {
        this.host = host;
        this.password = password;
        this.port = port;   
    }
}
