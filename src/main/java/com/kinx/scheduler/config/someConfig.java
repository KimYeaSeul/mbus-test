package com.kinx.scheduler.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class someConfig {
    @Bean
    public RestTemplate restTpl() {
        return new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofSeconds(300))
                .setReadTimeout(Duration.ofSeconds(1000))
                .build();
    }
}
