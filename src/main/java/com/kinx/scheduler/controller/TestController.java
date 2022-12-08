package com.kinx.scheduler.controller;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
public class TestController {

    @GetMapping(value="/")
    public String mainPage(){
        return "HELLO WORLD";
    }

    @PostMapping(value="/callback")
    public String callbackApi(@RequestBody HashMap<String, Object> requestb){
        System.out.println(requestb);
        return requestb.toString();
    }
}
