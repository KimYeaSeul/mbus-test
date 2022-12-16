package com.kinx.scheduler.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kinx.scheduler.dto.GetToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

@Slf4j
@Service
public class TokenService {
    private HttpURLConnection con;
    GetToken gt = new GetToken();
    public String tokenRequest(){
        String expire = String.valueOf((System.currentTimeMillis() / 1000) + 86400); // 1시간 = 3600, 24시간 = 86400
        // HttpURLConnection 방법
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] target = (email+":"+apikey).getBytes();
        String auth = new String(encoder.encode(target));

        try {
            log.info("================= get 요청 시작 =============================");
            String reqUrl = baseUrl +"/v2/token?expire=" + expire;
            URL url = new URL(reqUrl);

            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Authorization", "Basic "+auth);

            log.info("request URL : " + reqUrl);
            log.info("auth val : " +auth);

            log.info("================= get 요청 끝, 응답 받기 시작 =============================");
            int status = con.getResponseCode();
            log.info("code : " + status);

            if(status == 200) {
                InputStream is = con.getInputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(is));
                String line;
                while ((line = in.readLine()) != null) {
                    ObjectMapper om = new ObjectMapper();
                    gt = om.readValue(line, GetToken.class);
                    log.info("getToken : "+ gt.getToken());
                    log.info("expire : "+ gt.getExpire());
                }
                in.close();
                return gt.getToken();
            }else{
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally{
            con.disconnect();
            log.info("================= Token Service finally =============================");
        }
    }
}
