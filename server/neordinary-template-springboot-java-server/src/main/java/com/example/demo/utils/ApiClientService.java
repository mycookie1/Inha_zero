package com.example.demo.utils;

import com.example.demo.common.exceptions.BaseException;
import com.example.demo.common.response.BaseResponse;
import com.example.demo.common.response.BaseResponseStatus;
import com.example.demo.common.secret.Secret;
import com.example.demo.src.Open.OpenController;
import com.example.demo.src.Open.dto.GetOpenAIReqDto;
import com.example.demo.src.Open.dto.Message;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;

//@Service
public class ApiClientService {
    public String sendPostRequest(String imageUrl) {
        // API 엔드포인트 및 요청 정보 설정
        String apiUrl = "https://eqkyhw38bo.apigw.ntruss.com/custom/v1/23707/5503778efc7906255bde5996d820ed721e318f1873624979d4f420eab2c5ae91/general";
        //String xOcrSecret = Secret.CLOVA_SECRET_KEY;
        String xOcrSecret = "ZlFkalZBb05YaUtKWVRpT0R6RXptVnpvZEpQVmpYUFk=";
        //String imageUrl = "https://greencoderbucket.s3.ap-northeast-2.amazonaws.com/%EC%98%81%EC%88%98%EC%A6%9D2.jpg";

        // HTTP 요청을 위한 RestTemplate 객체 생성
        RestTemplate restTemplate = new RestTemplate();

        // 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-OCR-SECRET", xOcrSecret);

        // 요청 바디 설정
        String requestBody = "{\n" +
                "    \"images\": [\n" +
                "      {\n" +
                "        \"format\": \"png\",\n" +
                "        \"name\": \"medium\",\n" +
                "        \"data\": null,\n" +
                "        \"url\": \"" + imageUrl + "\"\n" +
                "      }\n" +
                "    ],\n" +
                "    \"lang\": \"ko\",\n" +
                "    \"requestId\": \"string\",\n" +
                "    \"resultType\": \"string\",\n" +
                "    \"timestamp\": " + System.currentTimeMillis() + ",\n" +
                "    \"version\": \"V1\"\n" +
                "}";

        // RequestEntity 객체 생성
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        // API 요청 보내기
        ResponseEntity<Map> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, Map.class);
        String answer = "";
        // 응답 처리
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            Map<String, Object> responseBody = responseEntity.getBody();
            String str[] = responseBody.get("images").toString().split("inferText=");

            for(String s: str){
                String temp[] = s.split(",");
                answer += (" ,"+temp[0]);
            }
            System.out.println("spilt   " + answer);
            System.out.println("응답 성공: " + responseBody.get("images"));
        } else {
            System.out.println("응답 실패: " + responseEntity.getStatusCode());
        }
        getOpenAi(answer);
        return answer;
    }

    private static void getOpenAi(String keyword) {
        String ENDPOINT = "https://api.openai.com/v1/chat/completions";
        // 프로프트 생성
        String prompt = keyword +"에서 상품명만 추출해서 ','로 구분해줘, 상품명이외는 제거해줘, 상품명외의 형태는 제거해ㄴ";

        // header 등록: Content-Type, Authorization(API KEY), Message
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        //TODO: 시크릿키 등록
        headers.set("Authorization", "Bearer " + Secret.OPEN_API_SECRET_KEY);

        Map<String, Object> requestBody = new HashMap<>();

        // Message: model, role
        requestBody.put("model", "gpt-3.5-turbo");
        Message message = new Message("user", prompt);
        Message[] messages = {message};
        requestBody.put("messages", messages);
        try {
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> response = restTemplate.postForEntity(ENDPOINT, requestEntity, Map.class);
            // answer 추출
            String[] responses = response.getBody().get("choices").toString().split("role=assistant, content=|}, finish_reason=stop");
            GetOpenAIReqDto getOpenAIResDto = new GetOpenAIReqDto(responses[1]);
            System.out.println(responses[1]);
        }
        catch (Exception e){
            throw new BaseException(BaseResponseStatus.OPENAI_ERROR);
        }
    }


}