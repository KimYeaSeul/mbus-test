package com.kinx.scheduler.service.media;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kinx.scheduler.dto.GetListCategory;
import com.kinx.scheduler.dto.ResponseDto;
import com.kinx.scheduler.service.HttpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class _1AllCategoryService {
    private final HttpService httpService;
    private final RestTemplate restTpl;
    @Value("${my.url}")
    private String baseUrl;
    @Value("${my.categoryId}")
    private int categoryId;
    public void createCategory(){

    }

    public void listCategory(String token) throws JsonProcessingException {
        String url = baseUrl + "/v2/media";
        ResponseEntity<Map> getReq = httpService.getRequest(token, url);
//        ResponseDto listCategory = new ResponseDto<Map<String, ?>>(getReq);

        ObjectMapper mapper = new ObjectMapper();
//        Map<String, List<GetListCategory>> myMaps = new HashMap<String, List<GetListCategory>>();
        Map<String, List<GetListCategory>> jsonInMap= mapper.convertValue(getReq.getBody(), Map.class);
        ResponseDto responseDto = new ResponseDto(jsonInMap);
//        log.info(listCategory.toString());
//        log.info(getReq.toString());
//        String categoryname = mapper.writeValueAsString(jsonInMap.get("category_list").get(0).getCategory_name());
        GetListCategory gettest = responseDto.getAttributeslistCategory().get(0);
        log.info(gettest.getCategory_name());
    }
}
