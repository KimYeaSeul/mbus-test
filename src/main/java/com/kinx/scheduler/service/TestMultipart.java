package com.kinx.scheduler.service;

import com.google.gson.Gson;
import com.kinx.scheduler.dto.GetToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.HashMap;

@Slf4j
@Component
public class TestMultipart {
    /**********************************************************************
     * 기본설정
     *********************************************************************/
    private static final int readByteSize = 256;
    private static final String charset= "UTF-8";
    private static final String CRLF= "\r\n";
    private PrintWriter writer = null;
    private OutputStream output = null;

    private Gson gson = new Gson();
//    private GetToken gt = new GetToken();
/**********************************************************************
 * 응답코드 정의
 *********************************************************************/
    //에러메시지 정의(이것은 편한데로 고쳐주세요)
    @SuppressWarnings("serial")
    private static final HashMap<String,String> errorObj = new HashMap<String,String>(){{
        put("200", "정상");
        put("999", "실패");
    }};
    //응답결과 - 실패가정 후 진행
    private HashMap<String,Object> responseBody = new HashMap<String,Object>(){{
        put("errorCode", "999");
        put("errorMsg", errorObj.get("999"));
    }};
    private void setHttpStatus(int httpStatus){
        this.responseBody.put("httpStatus", httpStatus);
    };
    private void setResponseBody(HashMap<String,String> responseBody){
        this.responseBody.putAll(responseBody);
    };

    private void addResponseDetail(String detail){
        this.responseBody.put("detail", detail);
    };

    /**********************************************************************
     * 파일전송
     **********************************************************************/
    @SuppressWarnings("unchecked")
    public void send_file_execute(String token) throws IOException {

        //파라미터
//        String param1 = param.get("paramaram1").toString();//param1
//        String param2= "param2";//param2
//        String param3 = "param3";//param3
//        String param4 = param.get("param4").toString();
//        File file1 = (File) param.get("file1");

        String boundary = this.setBoundary(); //boundary정의

        URL url = new URL(testUrl);
        log.info("접속시도(URL) : "+ testUrl);
        log.info("Token : "+token);
        HttpURLConnection connection;
//        if(testUrl.startsWith("https")) {
//            connection = (HttpsURLConnection) url.openConnection();
//        }else {
            connection = (HttpURLConnection) url.openConnection();
//        }

        try{
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            connection.setRequestMethod("POST");
//            connection.setRequestProperty("Content-Type", "multipart/form-data");
            connection.setRequestProperty("X-Mbus-Token", token);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setConnectTimeout(10000);

            this.output = connection.getOutputStream();
            this.writer =  new PrintWriter(new OutputStreamWriter(this.output, charset), true);
            //전송 정보
            System.out.println("");
//            System.out.println("file1: "+ file1.getName());
            System.out.println("base path " + new File("src/main/resources/static/cat.mp4").getAbsolutePath());
            File file2 = new File("src/main/resources/static/cat.mp4");
            //전송
//            addString(boundary, "param1", param1);
//            addString(boundary, "param2", param2);
//            addString(boundary, "param3", param3);
//            addString(boundary, "param4", param4);
            addFile(boundary, "file", file2);
            addEnd(boundary);

            // Request is lazily fired whenever you need to obtain information about response.
            int httpStatus = connection.getResponseCode();
            log.info(String.valueOf(httpStatus));
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), charset));
                StringBuilder sb = new StringBuilder();
                String line = "";
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                System.out.println("Response: " + sb.toString());
                log.info("Response: " + sb.toString());
                //★★★
//                setHttpStatus(httpStatus);
            setResponseBody(gson.fromJson(URLDecoder.decode(sb.toString(), charset), HashMap.class));


        }catch(Exception e){
            log.info(e.getMessage());
            int httpStatus = connection.getResponseCode();
            setHttpStatus(httpStatus);
            addResponseDetail(e.getMessage());
            e.printStackTrace();

        }finally{
            this.writer.close();
            this.output.close();
            connection.disconnect();
        }
    }

    /**********************************************************************
     * 통신 관련
     **********************************************************************/
    //바운더리 셋팅
    private String setBoundary(){
        String boundaryTime = Long.toHexString(System.currentTimeMillis()); // Just generate some unique random value.
        String boundary = "ys"+boundaryTime;
        return boundary;
    }
    //스트링 추가
    private void addString(String boundary, String _key, String _value){// Send normal String
        StringBuilder sb = new StringBuilder();
        sb.append("--"+ boundary).append(CRLF);
        sb.append("Content-Disposition: form-data; name=\""+ _key +"\"").append(CRLF);
        sb.append("Content-Type: text/plain; charset=" + charset).append(CRLF);
        sb.append(CRLF).append(_value).append(CRLF);

        this.writer.append(sb).flush();
    }
    //파일 추가
    private void addFile(String boundary, String _key, File _file) throws IOException{// Send File
        log.warn("filename : " + _file.getName());
        StringBuilder sb = new StringBuilder();
        sb.append("--"+ boundary).append(CRLF);
        sb.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + _file.getName() + "\"").append(CRLF);
//        sb.append("Content-Type: "+ URLConnection.guessContentTypeFromName(_file.getName())).append(CRLF); // Text file itself must be saved in this charset!
//        sb.append("Content-Transfer-Encoding: binary").append(CRLF);
        sb.append(CRLF);
        log.warn(sb.toString());
        this.writer.append(sb).flush();

        FileInputStream inputStream = new FileInputStream(_file);
        byte[] buffer = new byte[(int)_file.length()];
        int bytesRead = -1;
        while((bytesRead = inputStream.read(buffer)) != -1){
            this.output.write(buffer, 0, bytesRead);
        }
        this.output.flush();
        inputStream.close();

        this.writer.append(CRLF).flush();
    }
    //전송처리 끝
    private void addEnd(String boundary){//End of multipart/form-data.
        StringBuilder sb = new StringBuilder();
        sb.append("--").append(boundary).append("--").append(CRLF);
        this.writer.append(sb).flush();
    }
}
