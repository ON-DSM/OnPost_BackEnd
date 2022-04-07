package com.onpost.global.jwt;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.Base64;

@Getter
@ConstructorBinding
@ConfigurationProperties("jwt")
public class JwtProperties {

    private final String header;
    private final String secretKey;
    private final String AUTHORIZATION_KEY;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;

    public JwtProperties(String key, String secret, Long access, Long refresh, String header) {
        this.accessTokenExpiration = access * 1000;
        this.refreshTokenExpiration = refresh * 1000;
        this.header = header;
        this.secretKey = Base64.getEncoder().encodeToString(secret.getBytes());
        this.AUTHORIZATION_KEY = key;
    }
}
