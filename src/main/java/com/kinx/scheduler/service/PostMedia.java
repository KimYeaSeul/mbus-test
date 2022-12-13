package com.kinx.scheduler.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;

@Slf4j
@Service
public class PostMedia {
    private PrintWriter writer = null;
    private OutputStream output = null;
    public String mediaUploadRequest(String token) throws JsonProcessingException {
        //Spring restTemplate 방법
        RestTemplate restTemplate = new RestTemplate();
        String url = baseUrl + "/v2/media/" + categoryId;

        MultipartBodyBuilder multipartBodyBuilder = new MultipartBodyBuilder();
        Resource file2 = new ClassPathResource("/static/cat.mp4");
        multipartBodyBuilder.part("file", file2, MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, HttpEntity<?>> multipartBody = multipartBodyBuilder.build();

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.MULTIPART_FORM_DATA);
        header.add("X-Mbus-Token",token);
//        header.add("Content-Type","multipart/form-data; boundary=" + boundary);

        log.info("Req Header : "+ header.toString());
        HttpEntity<?> entity = new HttpEntity<>(multipartBody, header);

        ResponseEntity<?> resultMap = restTemplate.postForEntity(url, entity, String.class);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("statusCode", resultMap.getStatusCodeValue()); //http status code를 확인
        result.put("header", resultMap.getHeaders()); //헤더 정보 확인
        result.put("body", resultMap.getBody()); //실제 데이터 정보 확인

        //데이터를 제대로 전달 받았는지 확인 string형태로 파싱해줌
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = mapper.writeValueAsString(resultMap.getBody());

        return jsonInString;
    }
}
