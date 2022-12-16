package com.kinx.scheduler.controller;

import com.kinx.scheduler.domain.CallbackData;
import com.kinx.scheduler.dto.GetPostResponse;
import com.kinx.scheduler.repository.CallbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@RequiredArgsConstructor
@RestController
public class TestController {
    private final  CallbackRepository callbackRepository;
    @GetMapping(value="/")
    public String mainPage(){
        return "HELLO WORLD";
    }

    @PostMapping(value="/callback")
    public String callbackApi(@RequestBody GetPostResponse dto){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
        TimeZone tz = TimeZone.getTimeZone("Asia/Seoul");
        dateFormat.setTimeZone(tz);
        System.out.println("Hello im callback !"+dateFormat.format(new Date()));
        System.out.println("RequestBody = " + dto);
        CallbackData dd = new CallbackData(dto);
        System.out.println("new CallbackData = " +dd);
        callbackRepository.save( dd);
        System.out.println("----------------------------------------------------------------------------------");
        return dto.toString();
    }
}
