package com.kinx.scheduler.task;

import com.kinx.scheduler.domain.ReqUploadMedia;
import com.kinx.scheduler.repository.ReqUploadMediaRepository;
import com.kinx.scheduler.service.PostMedia;
import com.kinx.scheduler.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Slf4j
@Component
@EnableAsync
public class Scheduler {
    @Autowired
    private ReqUploadMediaRepository umRepository;
    TokenService tokenService = new TokenService();
    PostMedia postMedia = new PostMedia();

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//    private final ReqUploadMediaRepository reqUploadMediaRepository;
    private final LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

    private String token = "";
    public Scheduler() {
//        this.reqUploadMediaRepository = reqUploadMediaRepository;
//        this.postMedia = new PostMedia(this.reqUploadMediaRepository);
        // token 생성
        this.token = tokenService.tokenRequest();
    }

    @Async
    @Scheduled(fixedRate = 300000)
    public void scheduleTaskUsingCronExpression() throws IOException, ParseException {
        String localDateTimeFormat2
                = now.format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")
        );

        // upload 요청 return 401 or 201
        Integer uploadResult = postMedia.postMedia(this.token);
        // if 401 - token 생성
        if(uploadResult == 401){
            this.token = tokenService.tokenRequest();
        // else if 201 - get midia list
        }else if(uploadResult == 201){
            String mediaId = postMedia.getMediaList(this.token);
            Resource file = new ClassPathResource("/static/cat.mp4");
            ReqUploadMedia rum = ReqUploadMedia.builder()
                    .filename(file.getFilename())
                    .size(file.getFile().length())
                    .mediaId(mediaId)
                    .build();
            umRepository.save(rum);
        }
        log.info("스케쥴러 작동 시작 ! 현재 시각 : "+dateFormat.format(new Date()));
        log.info("now date " + localDateTimeFormat2);
//        log.info("upload Media Success ! ___" + postMedia.postMedia());
    }
}
