package com.onpost.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;

@Configuration
@RequiredArgsConstructor
public class JPAQueryConfig {

    private final EntityManager entityManager;

    @Bean
    public JPAQueryConfig jpaQueryConfig() {
        return new JPAQueryConfig(entityManager);
    }
}
