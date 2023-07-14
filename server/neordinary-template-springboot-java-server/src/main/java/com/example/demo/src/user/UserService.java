package com.example.demo.src.user;

import com.example.demo.common.exceptions.BaseException;
import com.example.demo.common.response.BaseResponse;
import com.example.demo.common.response.BaseResponseStatus;
import com.example.demo.common.secret.Secret;
import com.example.demo.src.Data.DataRepository;
import com.example.demo.src.Data.Entity.Data;
import com.example.demo.src.Open.dto.GetOpenAIReqDto;
import com.example.demo.src.Open.dto.Message;
import com.example.demo.src.tree.TreeRepository;
import com.example.demo.src.tree.entity.Tree;
import com.example.demo.src.user.DTO.*;
import com.example.demo.src.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final TreeRepository treeRepository;
    private final DataRepository dataRepository;
    @Transactional
    public GetTreeDetail getOneTree(Long userId) throws BaseException {

        User user;
        try {
            user = userRepository.findUserByUserId(userId);
            System.out.println(user.getUserId());
        }catch (Exception exception) {
            log.error(exception.getMessage());
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }

        int index = user.getTrees().size();
        Tree tree = user.getTrees().get(index-1);
        GetTreeDetail getTreeDetail = new GetTreeDetail(user.getScore(), tree.getScore(), tree.getColor(), tree.getSize());

        return getTreeDetail;

    }
    @Transactional
    public GetTrees getTrees(Long userId) throws BaseException {

        User user;
        try {
            user = userRepository.findUserByUserId(userId);
            System.out.println(user.getUserId());
        }catch (Exception exception) {
            log.error(exception.getMessage());
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
        int index = user.getTrees().size();
        Tree tree = user.getTrees().get(index-1);

        List<Tree> treeList = treeRepository.findAllByUserId(user);
        treeList.stream().sorted(Comparator.comparing(Tree::getTreeId));
        if(tree.getScore() < 50){
            treeList.remove(index-1);
        }
        List<GetOneTree> getOneTreeList = treeList.stream().map(e -> new GetOneTree(e.getSize(),e.getColor())).collect(Collectors.toList());

        GetTrees getTrees= new GetTrees(user.getScore(),tree.getScore(),getOneTreeList,new GetOneTree(tree.getSize(), tree.getColor()));

        return getTrees;
    }

    public String createTree(Long userId) throws BaseException {

        User user;
        try {
            user = userRepository.findUserByUserId(userId);
            System.out.println(user.getUserId());
        }catch (Exception exception) {
            log.error(exception.getMessage());
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
        List<Tree> treeList = treeRepository.findAllByUserId(user);


        Random rand = new Random();
        int color = rand.nextInt(3);
        Tree tree = new Tree(1,color+1,0);
        tree.setUserId(user);
        Tree savedTree = treeRepository.save(tree);

        return savedTree.getTreeId()+"번 나무생성";
    }
    @Transactional
    public GetScore getScore(Long userId, Integer score) throws BaseException {
        User user;
        try {
            user = userRepository.findUserByUserId(userId);
            System.out.println(user.getUserId());
        }catch (Exception exception) {
            log.error(exception.getMessage());
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
        int index = user.getTrees().size();
        Tree tree = user.getTrees().get(index-1);
        System.out.println("now tree score "+ tree.getScore());
        System.out.println("now score "+ score);

        List<Tree> treeList = treeRepository.findAllByUserId(user);
        GetScore getScore = new GetScore(0,false,0,false);
        if(tree.getScore() + score >= 50){
            System.out.println("50dltkd "+ tree.getScore() + score);

            int sum = tree.getScore() + score;
            tree.setScore(50);
            Random rand = new Random();
            int color = rand.nextInt(3);
            Tree newTree = new Tree(1,color+1,sum - 50);
            newTree.setUserId(user);
            if(20 <= sum - 50 && sum - 50 < 40){
                newTree.setSize(2);
            }
            else if(sum - 50 >= 40){
                newTree.setSize(3);
            }
            treeRepository.save(newTree);
            getScore.setCurrentTreeScore(sum-50);
            getScore.setCurrentTreeScoreIs(true);
        }
        else{
            System.out.println("50gfdglkd "+ tree.getScore() + score);
            tree.setScore(tree.getScore() + score);
            if(20 <= tree.getScore() && tree.getScore() < 40){
                tree.setSize(2);
            }
            else if( tree.getScore() >= 40){
                tree.setSize(3);
            }
            getScore.setCurrentTreeScore(tree.getScore());
        }
        if(user.getScore() + score >= 150){
            Integer temp = user.getScore() + score;
            user.setScore(user.getScore() + score - 150);
            getScore.setTotalScoreIs(true);
            getScore.setTotalScore(temp - 150);
        }
        else{
            user.setScore(user.getScore() + score);
            getScore.setTotalScore(user.getScore());
        }
        userRepository.save(user);
        treeRepository.save(tree);
        return getScore;
    }
    public GetAllReceipt getReceipt(String imageUrl) throws BaseException {

        String temp = sendPostRequest(imageUrl);
        String[] names = temp.split(",");
        List<GetReceipt> receiptList = new ArrayList<>();
        Integer sum = 0;
        for(String name: names){
            System.out.println("name  "+name);
           List<Data> dataList = dataRepository.findByNameContaining(name);
           if(dataList.size() > 1){
               Data data = dataList.get(0);
               receiptList.add(new GetReceipt(data.getName(), data.getCategory(),(data.getCategory()=="저탄소제품") ? 3 : 1 ,1));
               sum += ((data.getCategory()=="저탄소제품") ? 3 : 1);
           }
           else{
               receiptList.add(new GetReceipt(name, "",0 ,1));
           }
        }
        GetAllReceipt getAllReceipt = new GetAllReceipt(receiptList,sum);
        return getAllReceipt;
    }

    public static String sendPostRequest(String imageUrl1) {
        // API 엔드포인트 및 요청 정보 설정
        String apiUrl = "https://eqkyhw38bo.apigw.ntruss.com/custom/v1/23707/5503778efc7906255bde5996d820ed721e318f1873624979d4f420eab2c5ae91/general";
        //String xOcrSecret = Secret.CLOVA_SECRET_KEY;
        String xOcrSecret = "ZlFkalZBb05YaUtKWVRpT0R6RXptVnpvZEpQVmpYUFk=";
        //        System.out.println("imageUrl  " + imageUrl);
        String imageUrl = "https://greencoderbucket.s3.ap-northeast-2.amazonaws.com/%EC%98%81%EC%88%98%EC%A6%9D2.jpg";

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
        answer = getOpenAi(answer);
        return answer;
    }

    private static String getOpenAi(String keyword) {
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
            System.out.println("openai " + responses[1]);
            return responses[1];
        }
        catch (Exception e){
            throw new BaseException(BaseResponseStatus.OPENAI_ERROR);
        }
    }
}
