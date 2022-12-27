package com.kinx.scheduler.task;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kinx.scheduler.dto.GetMediaList;
import com.kinx.scheduler.dto.GetMediaListArray;
import com.kinx.scheduler.service.DeleteService;
import com.kinx.scheduler.service.authentication.TokenService;
import com.kinx.scheduler.service.media._1AllCategoryService;
import com.kinx.scheduler.service.media._2CategoryService;
import com.kinx.scheduler.service.media._3MediaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Slf4j
@Component
@RequiredArgsConstructor
@EnableAsync
public class Scheduler {
    private final TokenService tokenService;
    private final _1AllCategoryService allCategoryService;
    private final _3MediaService mediaService;
    private final _2CategoryService categoryService;
    private final DeleteService deleteService;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
    private String token = "";

    @Async
    @Scheduled(fixedRate = 300000)
    public void mediaUpload() throws JsonProcessingException {
        if(this.token == "") this.token = tokenService.createToken();
        log.info("현재 시각 : {} ", dateFormat.format(new Date()));
        final long totalMemory = Runtime.getRuntime().totalMemory()/1024/1024;
        final long freeMemory = Runtime.getRuntime().freeMemory()/1024/1024;
        final long maxMemory = Runtime.getRuntime().maxMemory()/1024/1024;
        final long usedMemory = totalMemory - freeMemory;

        log.info("total memory: " + totalMemory + "M bytes");
        log.info("free memory: " + freeMemory + "M bytes");
        log.info("max memory: " + maxMemory + "M bytes");
        log.info("used memory: " + usedMemory + "M bytes");
//        allCategoryService.listCategory(this.token);
        if(this.token == "") this.token = tokenService.createToken();
//         upload 요청 return 401 or 201
        Integer uploadResult = categoryService.uploadMedia(this.token);
        // if 401 - token 생성
        if(uploadResult == 401){
            this.token = tokenService.createToken();
        // else if 201 - get midia list
        }
    }
    @Async
    @Scheduled(cron= "0 0 */6 * * *")
    public void getMediaDelete() {
//        if(this.token == "") this.token = tokenService.createToken();
        Calendar calendar = new GregorianCalendar();
        this.token="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJreXM5MTJAa2lueC5uZXQiLCJpYXQiOjE2NzIxMDM0MzQsImV4cCI6MTY3MjE4OTgzM30.horGJZXXexU8mIb9GmogkRMu4maZCVA9iCKyLqwVTC4";
        SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMddHHmmss");
        calendar.add(Calendar.DATE, - 1);
        String chkDate = SDF.format(calendar.getTime());


        GetMediaList mediaList = categoryService.listMedia(this.token);
        int length = mediaList.getMedia_list().length-1;
        Date yesterday = null;
        Date created = null;
        // 예외처리 밖으로 뺄수 있으면 빼보자구리!
        int deleteCount = 0;
        for(int i=length; i>0; i--) {
            GetMediaListArray media = mediaList.getMedia_list()[i];
            String resStr = "";
            try {
                yesterday = SDF.parse(chkDate);
                created = SDF.parse(media.getCreated());
            } catch (ParseException e) {
                log.error(" delete error = {}", e.getMessage());
            }
            if (created.before(yesterday)) {
                resStr = mediaService.deleteMedia(this.token, mediaList.getCategory_id(), media.getMedia_id());
                if(resStr.equals("204")) deleteCount++;
            }else break;
        }
        log.info("allmedia : {} , success delete count = {}, now date time = {}" , length, deleteCount , dateFormat.format(new Date()));
    }
}
