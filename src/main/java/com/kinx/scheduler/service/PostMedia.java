package com.kinx.scheduler.service;

import com.kinx.scheduler.dto.GetToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.http.HttpRequest;
import java.nio.file.Path;

@Slf4j
@Service
public class PostMedia {
    private HttpURLConnection con;
    private Integer categoryId = 3433;
    private GetToken gt;

    HttpRequest httpRequest;
    private PrintWriter writer = null;
    private OutputStream output = null;
    public void mediaUploadRequest(){
        try {
            log.info("================= 미디어 upload 요청 시작 =============================");
            Path path = Path.of("static/cat.mp4");
            log.info(gt.getToken());
            String reqUrl = baseUrl + "/v2/media/" + categoryId;
            log.info(reqUrl);
            URL url = new URL(reqUrl);

            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "multipart/form-data");
            con.setRequestProperty("X-Mbus-Token", gt.getToken());

            con.setDoInput(true);
            con.setDoOutput(true);

            this.output = con.getOutputStream();
            this.writer =  new PrintWriter(new OutputStreamWriter(this.output, "UTF-8"), true);

            StringBuilder sb = new StringBuilder();
            sb.append("Content-Disposition: form-data;").append("\\r\\n");
            this.writer.append(sb).flush();

        } catch(IOException e){

        }
    }
}
