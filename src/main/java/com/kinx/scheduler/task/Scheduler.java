package com.kinx.scheduler.task;

import com.kinx.scheduler.dto.GetMediaList;
import com.kinx.scheduler.dto.GetMediaListArray;
import com.kinx.scheduler.repository.ReqUploadMediaRepository;
import com.kinx.scheduler.service.PostMedia;
import com.kinx.scheduler.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Slf4j
@Component
@EnableAsync
public class Scheduler {
    @Autowired
    private ReqUploadMediaRepository umRepository;
    TokenService tokenService = new TokenService();
    @Autowired
    PostMedia postMedia;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
    private String token = "";
    public Scheduler() {
        // token 생성
        this.token = tokenService.tokenRequest();
    }

    @Async
    @Scheduled(fixedRate = 300000)
    public void scheduleTaskUsingCronExpression() throws IOException {
        String localDateTimeFormat2
                = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));

        // upload 요청 return 401 or 201
        Integer uploadResult = postMedia.postMedia(this.token);
        // if 401 - token 생성
        if(uploadResult == 401){
            this.token = tokenService.tokenRequest();
        // else if 201 - get midia list
        }
        log.info("스케쥴러 작동 시작 ! 현재 시각 : "+dateFormat.format(new Date()));
        log.info("now date " + localDateTimeFormat2);
    }
    @Async
    @Scheduled(cron= "* * 0/12 * * *")
    public void getMediaDelete(){

        Calendar calendar = new GregorianCalendar();
        SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMddHHmmss");
        calendar.add(Calendar.DATE, - 1);
        String chkDate = SDF.format(calendar.getTime());


        GetMediaList mediaList = postMedia.getMediaList(this.token);
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
                log.error(" delete error " + e.getMessage());
            }
            if (created.before(yesterday)) {
                resStr = postMedia.deleteMedia(this.token, mediaList.getCategory_id(), media.getMedia_id());
                if(resStr.equals("204")) deleteCount++;
            }else break;
        }
        log.info("success delete count = " + deleteCount +", now date time = " + dateFormat.format(new Date()));
    }
}
