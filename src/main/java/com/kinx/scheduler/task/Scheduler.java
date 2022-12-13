package com.kinx.scheduler.task;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kinx.scheduler.service.PostMedia;
import com.kinx.scheduler.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Component
@EnableAsync
public class Scheduler {
    TokenService tokenService = new TokenService();
    PostMedia postMedia = new PostMedia();
    private String token = "";
    public Scheduler() {
        this.token = tokenService.tokenRequest();
    }

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * Cron 표현식을 사용한 작업 예약
     * 초(0-59) 분(0-59) 시간(0-23) 일(1-31) 월(1-12) 요일(0-7)
     */

    @Async
    @Scheduled(fixedRate = 300000)
    public void scheduleTaskUsingCronExpression() throws JsonProcessingException {
        log.info("스케쥴러 작동 시작 ! 현재 시각 : "+dateFormat.format(new Date()));
        if(token != null ) log.info("upload Media Success ! ___"+postMedia.mediaUploadRequest(token));
    }
}
