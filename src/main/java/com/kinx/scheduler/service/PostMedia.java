package com.kinx.scheduler.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.http.HttpRequest;
import java.nio.file.Path;

@Slf4j
@Service
public class PostMedia {
    private HttpURLConnection con;
    private Integer categoryId = 3434;


    HttpRequest httpRequest;
    private PrintWriter writer = null;
    private OutputStream output = null;
    public void mediaUploadRequest(String token){
        try {
            log.info("================= 미디어 upload 요청 시작 =============================");
            Path path = Path.of("static/cat.mp4");
            log.info("receive token " +token);
            String reqUrl = baseUrl + "/v2/media/" + categoryId;
            log.info("req URL " + reqUrl);
            URL url = new URL(reqUrl);

            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "multipart/form-data");
            con.setRequestProperty("X-Mbus-Token", token);

            log.info("================= get 요청 끝, 응답 받기 시작 =============================");
            int status = con.getResponseCode();
            log.info(String.valueOf(status));
            log.info(url.toString());
            log.info(con.getResponseMessage());

            if(status == 200) {
                con.setDoInput(true);
                con.setDoOutput(true);
                this.output = con.getOutputStream();
                this.writer = new PrintWriter(new OutputStreamWriter(this.output, "UTF-8"), true);

                StringBuilder sb = new StringBuilder();
                sb.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + path + "\"").append("\\r\\n");
                this.writer.append(sb).flush();
            }else if(status == 400) {
                log.info("status is 40000000000");
            }
        } catch(IOException e){
            log.info("안돼ㅔㅔㅔ");
        }
    }
}
