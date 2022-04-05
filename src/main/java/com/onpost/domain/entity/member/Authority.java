package com.onpost.domain.entity.member;

import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

@AllArgsConstructor
public enum Authority {

    USER("USER"),
    ADMIN("ADMIN");

    private final String type;

    public List<GrantedAuthority> getAuth() {
        return List.of(new SimpleGrantedAuthority(this.type));
    }
}
