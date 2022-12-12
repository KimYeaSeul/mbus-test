package com.kinx.scheduler.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kinx.scheduler.dto.GetToken;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;

@Slf4j
public class Ec2ConnectionTest {

    private final long now = System.currentTimeMillis() / 1000; // timestamp 값
    private final String expire = String.valueOf(now + 7200); // 1시간 = 3600, 24시간 = 86400
    private HttpURLConnection con;

    GetToken gt = new GetToken();
    public String tokenRequest(){

        Base64.Encoder encoder = Base64.getEncoder();
        byte[] target = (email+":"+apikey).getBytes();
        String auth = new String(encoder.encode(target));

        try {
            log.info("================= get 요청 시작 =============================");
            String reqUrl = baseUrl +"/v2/token?expire=" + expire;
            log.info("request URL : " + reqUrl);
            URL url = new URL(reqUrl);

            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            log.info("auth val : " +auth);
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Authorization", "Basic "+auth);

            log.info("================= get 요청 끝, 응답 받기 시작 =============================");
            int status = con.getResponseCode();
            log.info("code : " + status);
            log.info("message : " + con.getResponseMessage());

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
            log.info("================= 무사히 finally =============================");
        }
    }

    String domain = "http://google.co.kr/GGGGGGGGG";
    public void conTest() {
        try {
            URL u = new URL(domain);
            HttpURLConnection con = (HttpURLConnection) u.openConnection();
            System.out.println("응답코드 : " + con.getResponseCode());
            System.out.println("응답메세지 : " + con.getResponseMessage());
        } catch (MalformedURLException e) {
            System.out.println(domain + " is not a URL I understand");
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(domain + " is not a URL I understand");
            System.out.println(e.getMessage());
        }
    }


}