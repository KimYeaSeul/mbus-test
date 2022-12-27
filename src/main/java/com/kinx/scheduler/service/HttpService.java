package com.kinx.scheduler.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class HttpService {
    @Value("${my.url}")
    private String baseUrl;
    @Value("${my.categoryId}")
    private int categoryId;

    private final RestTemplate restTpl;
    public ResponseEntity<Map> getRequest(String token, String url) throws JsonProcessingException {
        HttpHeaders headers = setHeaders(token, null);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<Map> responseEntity = restTpl.exchange(url, HttpMethod.GET, entity, Map.class);
        return responseEntity;
    }
    public MultiValueMap<String, HttpEntity<?>> setMultipartBody(Resource file){
        MultipartBodyBuilder multipartBodyBuilder = new MultipartBodyBuilder();
        multipartBodyBuilder.part("file", file, MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, HttpEntity<?>> multipartBody = multipartBodyBuilder.build();

        return multipartBody;
    }

    public HttpHeaders setHeaders(String token, MediaType contentType){
        HttpHeaders header = new HttpHeaders();
        header.setContentType(contentType);
        header.add("X-Mbus-Token",token);
        return header;
    }
}
