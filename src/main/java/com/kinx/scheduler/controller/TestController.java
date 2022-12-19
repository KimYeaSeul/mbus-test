package com.kinx.scheduler.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TestController {
    @GetMapping(value="/")
    public String mainPage(){
        return "HELLO WORLD";
    }
}
