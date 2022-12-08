package com.kinx.scheduler.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kinx.scheduler.dto.GetToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.*;
import java.util.Base64;

@Slf4j
@Service
public class TokenService {

    private final long now = System.currentTimeMillis() / 1000; // timestamp 값
    private final String expire = String.valueOf(now + 3600); // 1시간 = 3600, 24시간 = 86400
    private HttpURLConnection con;

    GetToken gt = new GetToken();
    public String tokenRequest(){

        Base64.Encoder encoder = Base64.getEncoder();
        byte[] target = (email+":"+apikey).getBytes();
        String auth = new String(encoder.encode(target));
        log.info(baseUrl);
        log.info(email);
        log.info(auth);

        try {
            log.info("================= get 요청 시작 =============================");
            String reqUrl = baseUrl +"/v2/token?expire=" + expire;
            log.info(reqUrl);
            URL url = new URL(reqUrl);

            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            log.info(auth);
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Authorization", "Basic "+auth);

//            Map<String, String> parameters = new HashMap<>();
//            parameters.put("expire", expire);
//            con.setDoOutput(true);
//            DataOutputStream out = new DataOutputStream(con.getOutputStream());
//            log.info(ParameterStringBuilder.getParamsString(parameters));
//            out.writeBytes(ParameterStringBuilder.getParamsString(parameters));
//            out.flush();
//            out.close();
            log.info("================= get 요청 끝, 응답 받기 시작 =============================");
            int status = con.getResponseCode();
            log.info(String.valueOf(status));
            log.info(url.toString());


            if(status == 200) {
                InputStream is = con.getInputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(is));
//                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    ObjectMapper om = new ObjectMapper();
                    gt = om.readValue(line, GetToken.class);
                    log.info(gt.getToken());
                    log.info(gt.getExpire());
                }
                in.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally{
            con.disconnect();
            log.info("================= 무사히 finally =============================");
            return gt.getToken();
        }
    }
}
