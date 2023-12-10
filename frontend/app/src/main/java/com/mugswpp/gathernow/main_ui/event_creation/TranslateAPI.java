package com.mugswpp.gathernow.main_ui.event_creation;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

// 네이버 기계 번역 (Papago SMT) API 예제
public class TranslateAPI {
    static String clientId = "";//애플리케이션 클라이언트 아이디값";
    static String clientSecret = "";//애플리케이션 클라이언트 시크릿값";

    public static void main(String[] args) {
        //String clientId = "";//애플리케이션 클라이언트 아이디값";
        //String clientSecret = "";//애플리케이션 클라이언트 시크릿값";

        String apiURL = "https://openapi.naver.com/v1/papago/n2mt";
        String text;
        try {
            text = URLEncoder.encode("안녕하세요. 오늘 기분은 어떻습니까?", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("인코딩 실패", e);
        }

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-NCP-APIGW-API-KEY-ID", clientId);
        requestHeaders.put("X-NCP-APIGW-API-KEY", clientSecret);

        String responseBody = post(apiURL, requestHeaders, text);

        System.out.println(responseBody);
    }


    public static String translate_address(String input){
        //String clientId = "";  // replace client id here
        //String clientSecret = ""; // replace client secret here

        String apiURL = "https://naveropenapi.apigw.ntruss.com/nmt/v1/translation";
        String text;
        try {
            text = URLEncoder.encode(input, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("인코딩 실패", e);
        }

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-NCP-APIGW-API-KEY-ID", clientId);
        requestHeaders.put("X-NCP-APIGW-API-KEY", clientSecret);

        String responseBody = post(apiURL, requestHeaders, text);

        try {
            // Create a JSONObject from the string
            JSONObject jsonObject = new JSONObject(responseBody);

            // Navigate through the hierarchy of the JSON to get the translated text
            JSONObject message = jsonObject.getJSONObject("message");
            JSONObject result = message.getJSONObject("result");
            String translatedText = result.getString("translatedText");
            return translatedText;

        } catch (JSONException e) {
            e.printStackTrace(); // Handle the exception appropriately
            Log.d("ApiPAPAGO", "Error parsing JSON data.");
        }
        return null;
    }

    private static String post(String apiUrl, Map<String, String> requestHeaders, String text){
        HttpURLConnection con = connect(apiUrl);
        String postParams = "source=ko&target=en&text=" + text; //원본언어: 한국어 (ko) -> 목적언어: 영어 (en)
        try {
            con.setRequestMethod("POST");
            for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }

            con.setDoOutput(true);
            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                wr.write(postParams.getBytes());
                wr.flush();
            }

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 응답
                return readBody(con.getInputStream());
            } else {  // 에러 응답
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }

    private static HttpURLConnection connect(String apiUrl){
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection)url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }

    private static String readBody(InputStream body){
        InputStreamReader streamReader = new InputStreamReader(body);

        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();

            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }

            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
        }
    }


}