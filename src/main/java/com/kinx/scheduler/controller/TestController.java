package com.kinx.scheduler.controller;

import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

@RestController
public class TestController {

    @GetMapping(value="/")
    public String mainPage(){
        return "HELLO WORLD";
    }

    @PostMapping(value="/callback")
    public String callbackApi(@RequestBody HashMap<String, Object> requestb){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        System.out.println("Hello im callback !"+dateFormat.format(new Date()));
        System.out.println(requestb);
        System.out.println("----------------------------------------------------------------------------------");
        return requestb.toString();
    }
}
