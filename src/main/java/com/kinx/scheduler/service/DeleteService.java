package com.kinx.scheduler.service;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.OutputStream;
import java.io.PrintWriter;

@Slf4j
@RequiredArgsConstructor
@Service
public class DeleteService {
    @Value("${my.url}")
    private String baseUrl;
    @Value("${my.categoryId}")
    private int categoryId;
    Gson gson = new Gson();

    private PrintWriter writer = null;
    private OutputStream output = null;

    private final HttpService httpService;
    private final RestTemplate restTpl;

}
