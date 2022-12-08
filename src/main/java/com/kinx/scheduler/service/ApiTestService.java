package com.kinx.scheduler.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kinx.scheduler.dto.GetToken;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
public class ApiTestService {
    long now = System.currentTimeMillis() / 1000;
    String expire = String.valueOf(now + 3600); // 1시간 = 3600, 24시간 = 86400
    HttpURLConnection con;
    URL url;
    public void tokenRequest() {
        try {
            log.info("================= get 요청 시작 =============================");
            String urlStr = "https://api-v2.midibus.dev-kinxcdn.com/v2/token?expire=" + expire;
            url = new URL(urlStr);

            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", "Basic a3lzOTEyQGtpbngubmV0OjE4NGU0ZWRmNjVjYTBiZjU=");
            con.setRequestProperty("Content-Type", "application/json");

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


            if (status == 200) {
                InputStream is = con.getInputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(is));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    ObjectMapper om = new ObjectMapper();
                    GetToken gt = om.readValue(line, GetToken.class);
                    log.info(gt.getToken());
                    log.info(gt.getExpire());
                }
                in.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            con.disconnect();
            log.info("================= 무사히 finally =============================");
        }
    }
}
