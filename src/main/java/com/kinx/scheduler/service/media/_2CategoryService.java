package com.kinx.scheduler.service.media;

import com.google.gson.Gson;
import com.kinx.scheduler.domain.ReqUploadMedia;
import com.kinx.scheduler.dto.GetMediaList;
import com.kinx.scheduler.repository.ReqUploadMediaRepository;
import com.kinx.scheduler.service.HttpService;
import com.kinx.scheduler.service.authentication.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.OutputStream;
import java.io.PrintWriter;

@Slf4j
@RequiredArgsConstructor
@Service
public class _2CategoryService {
    private final ReqUploadMediaRepository umRepository;
    private final TokenService tokenService;
    private final RestTemplate restTpl;
    private final HttpService httpService;
    @Value("${my.url}")
    private String baseUrl;
    @Value("${my.categoryId}")
    private int categoryId;
    Gson gson = new Gson();

    private PrintWriter writer = null;
    private OutputStream output = null;

    public Integer uploadMedia(String token){

        String[] mediaList = new String[]{"10mb.mp4","18mb.mp4","20mb.mp4","30mb.mp4","106mb.mp4","cat.mp4"};
        int random = (int)(Math.random() * mediaList.length);
        Resource file = new ClassPathResource("/static/"+mediaList[random]);
        log.info("Send Media : {} ",mediaList[random]);

        MultiValueMap<String, HttpEntity<?>> body = httpService.setMultipartBody(file);
        HttpHeaders headers = httpService.setHeaders(token, MediaType.MULTIPART_FORM_DATA);
        HttpEntity<?> entity = new HttpEntity<>(body, headers);

        Integer httpStatusCode = 0;
        String url = baseUrl + "/v2/media/" + categoryId;
        ResponseEntity<?> resultMap = null;
        try {
            resultMap = restTpl.postForEntity(url, entity, String.class);
            httpStatusCode = resultMap.getStatusCodeValue();
            log.info("message = {} , code = {}", resultMap.getBody().toString(), httpStatusCode);
            GetMediaList mediaId = listMedia(token);
            ReqUploadMedia rum = ReqUploadMedia.builder()
                    .filename(file.getFilename())
                    .size(file.getFile().length())
                    .mediaId(mediaId.getMedia_list()[0].getMedia_id())
                    .build();
            umRepository.save(rum);
        }catch (HttpClientErrorException e){
            httpStatusCode = e.getRawStatusCode();
            log.error("postMedia HttpClientErrorException");
            log.error("message = {}, stackTrace = {}" , e.getMessage(), e.getStackTrace());
        }catch (NullPointerException e) {
            httpStatusCode = null;
            log.error("postMedia NullPointerException");
            log.error("message = {}, stackTrace = {}" , e.getMessage(), e.getStackTrace());
        }finally{
            //데이터를 제대로 전달 받았는지 확인 string형태로 파싱해줌
//            ObjectMapper mapper = new ObjectMapper();
//            jsonInString = mapper.writeValueAsString(resultMap.getBody());
            return httpStatusCode;
        }
    }

    public GetMediaList listMedia(String token){
        String url = baseUrl+"/v2/media/" + categoryId;
        HttpHeaders headers = httpService.setHeaders(token, null);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<?> responseEntity = restTpl.exchange(url, HttpMethod.GET, entity, String.class);
        String body = responseEntity.getBody().toString();
        GetMediaList newMedia = gson.fromJson(body, GetMediaList.class);
        return newMedia;
    }
}
