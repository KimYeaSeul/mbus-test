package com.kinx.scheduler.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.kinx.scheduler.dto.GetMediaList;
import com.kinx.scheduler.repository.ReqUploadMediaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostMedia {

    TokenService tokenService = new TokenService();
    Gson gson = new Gson();
    @Autowired
    private ReqUploadMediaRepository umRepository;
    private final LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

    private PrintWriter writer = null;
    private OutputStream output = null;
//    private String token = "";
//    public PostMedia() throws IOException {
//        this.token = tokenService.tokenRequest();
//        HttpEntity<?> entity = mediaUploadRequest(this.token);
//    }
    public HttpEntity<?> mediaUploadRequest(String token) throws IOException {
        //Spring restTemplate 방법
        MultipartBodyBuilder multipartBodyBuilder = new MultipartBodyBuilder();
        Resource file = new ClassPathResource("/static/cat.mp4");
        multipartBodyBuilder.part("file", file, MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, HttpEntity<?>> multipartBody = multipartBodyBuilder.build();

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.MULTIPART_FORM_DATA);
        header.add("X-Mbus-Token",token);

        log.info("Req Header : "+ header.toString());
        HttpEntity<?> entity = new HttpEntity<>(multipartBody, header);

        return entity;
    }

    public Integer postMedia(String token) throws IOException{
        Integer httpStatusCode = 0;
//        token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJreXM5MTJAa2lueC5uZXQiLCJpYXQiOjE2NzA4OTY3MzMsImV4cCI6MTY3MDk0NDAxMX0.ek2NGztpMpNm9X8btENQOdkj_rJsG0niuWUiDIXJM-8";
        System.out.println("token : " + token);
        HttpEntity<?> entity = mediaUploadRequest(token);
        RestTemplate restTemplate = new RestTemplate();
        String url = baseUrl + "/v2/media/" + categoryId;
        String jsonInString = "";
        String localDateTimeFormat2
                = now.format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")
        );

        ResponseEntity<?> resultMap = null;
        try {
//            umRepository.save(rum);
            resultMap = restTemplate.postForEntity(url, entity, String.class);
            httpStatusCode = resultMap.getStatusCodeValue();
        }catch (HttpClientErrorException e){
            httpStatusCode = e.getRawStatusCode();
//            httpStatusCode = e.getStatusCode();
//            this.token = tokenService.tokenRequest();
//            entity = mediaUploadRequest(token);
//            umRepository.save(rum);
//            resultMap = restTemplate.postForEntity(url, entity, String.class);
        }finally{
            log.error("2" + resultMap);
//            System.out.println("update status code " + resultMap.getStatusCode());
            //데이터를 제대로 전달 받았는지 확인 string형태로 파싱해줌
//            ObjectMapper mapper = new ObjectMapper();
//            jsonInString = mapper.writeValueAsString(resultMap.getBody());
//            System.out.println(jsonInString);
            return httpStatusCode;
        }
//        if(resultMap.getStatusCodeValue() == 201){
//            System.out.println("created completed !" );
//            System.out.println(""+getMediaList());
//        }
    }

    public String getMediaList(String token) throws JsonProcessingException, ParseException {
        String url = baseUrl+"/v2/media/" + categoryId; // api url

//        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
//        httpRequestFactory.setConnectTimeout(30000); // 연결시간 초과
        //Rest template setting
        RestTemplate restTpl = new RestTemplate();
        HttpHeaders headers  = new HttpHeaders(); // 담아줄 header
        headers.add("X-Mbus-Token", token);
        HttpEntity<?> entity = new HttpEntity<>(headers); // http entity에 header 담아줌

        ResponseEntity<?> responseEntity = restTpl.exchange(url, HttpMethod.GET, entity, String.class);
        String body = responseEntity.getBody().toString();
        GetMediaList newMedia = gson.fromJson(body, GetMediaList.class);
        System.out.println(newMedia.getMedia_list()[0].getMedia_id());
//        umRepository.

        return newMedia.getMedia_list()[0].getMedia_id();
    }
}
