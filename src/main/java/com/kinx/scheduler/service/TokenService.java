package com.kinx.scheduler.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class TokenService {
    private final WebClient webClient;

    public TokenService(WebClient.Builder webClientBuilder){

    }
}
