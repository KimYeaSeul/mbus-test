package com.kinx.scheduler.service.media;

import com.google.gson.Gson;
import com.kinx.scheduler.repository.ReqUploadMediaRepository;
import com.kinx.scheduler.service.HttpService;
import com.kinx.scheduler.service.authentication.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Slf4j
@RequiredArgsConstructor
@Service
public class MediaService {

    private final LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
    private final ReqUploadMediaRepository umRepository;
    private final TokenService tokenService;
    public final _2CategoryService CategoryService;
    @Value("${my.url}")
    private String baseUrl;
    @Value("${my.categoryId}")
    private int categoryId;
    Gson gson = new Gson();

    private PrintWriter writer = null;
    private OutputStream output = null;

    RestTemplate restTpl = new RestTemplateBuilder()
            .setConnectTimeout(Duration.ofSeconds(300))
            .setReadTimeout(Duration.ofSeconds(1000))
            .build();
    HttpService httpService = new HttpService();

    public String deleteMedia(String token, int categoryId, String mediaId ){
        String url = baseUrl+"/v2/media/" + categoryId+"/"+mediaId;
        String resultStr = "";
        try{
            HttpHeaders headers = httpService.setHeaders(token, null);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<?> responseEntity = restTpl.exchange(url, HttpMethod.DELETE, entity, String.class);
            resultStr = String.valueOf(responseEntity.getStatusCodeValue());
        }catch (HttpClientErrorException e ){
            System.out.println(e.getMessage());
            resultStr = e.getMessage();
        }finally{
            return resultStr;
        }

    }


//    public GetMediaList getUploadList(String token){
//        String url = baseUrl+"/v2/media/" + categoryId; // api url
//
////        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
////        httpRequestFactory.setConnectTimeout(30000); // 연결시간 초과
//        //Rest template setting
//
//        HttpHeaders headers  = new HttpHeaders(); // 담아줄 header
//        headers.add("X-Mbus-Token", token);
//        HttpEntity<?> entity = new HttpEntity<>(headers); // http entity에 header 담아줌
//
//        ResponseEntity<?> responseEntity = restTpl.exchange(url, HttpMethod.GET, entity, String.class);
//        String body = responseEntity.getBody().toString();
//        GetMediaList newMedia = gson.fromJson(body, GetMediaList.class);
//        System.out.println(newMedia.getMedia_list()[0].getMedia_id());
////        umRepository.
//
////        return newMedia.getMedia_list()[0].getMedia_id();
//        return newMedia;
//    }

}
