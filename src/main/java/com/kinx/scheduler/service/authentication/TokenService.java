package com.kinx.scheduler.service.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kinx.scheduler.dto.GetToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${my.email}")
    private String email;
    @Value("${my.apikey}")
    private String apikey;
    @Value("${my.url}")
    private String baseUrl;
    GetToken gt = new GetToken();
    public String createToken(){
        String expire = String.valueOf((System.currentTimeMillis() / 1000) + 86400); // 1시간 = 3600, 24시간 = 86400
        // HttpURLConnection 방법
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] target = (email+":"+apikey).getBytes();
        String auth = new String(encoder.encode(target));
        try {
            log.info("================= token Request Start =============================");
            String reqUrl = baseUrl +"/v2/token?expire=" + expire;
            URL url = new URL(reqUrl);

            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Authorization", "Basic "+auth);
            int status = con.getResponseCode();
            log.info("respnse code : {}" , status);

            if(status == 200) {
                InputStream is = con.getInputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(is));
                String line;
                while ((line = in.readLine()) != null) {
                    ObjectMapper om = new ObjectMapper();
                    gt = om.readValue(line, GetToken.class);
                    log.info("getToken : {} ", gt.getToken());
                    log.info("expire : {}", gt.getExpire());
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
            log.info("================= Token Get Service finally =============================");
        }
    }
}
