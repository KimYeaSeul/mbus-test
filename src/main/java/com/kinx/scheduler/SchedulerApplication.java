package com.kinx.scheduler;

import com.kinx.scheduler.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j

@EnableScheduling
@SpringBootApplication
public class SchedulerApplication {
	public static void main(String[] args) {
		SpringApplication.run(SchedulerApplication.class, args);
//		TokenService ts = new TokenService();
//		ts.tokenRequest();
	}
}
