package com.onpost.domain.entity.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@AllArgsConstructor
@Builder
@RedisHash
public class RefreshToken {

    @Id
    private String email;

    @Indexed
    private String refreshToken;

    @Indexed
    private String Authority;

    @TimeToLive
    private Long timeToLive;

    public void updateToken(String token) {
        this.refreshToken = token;
    }
}
