package com.example.demo.src.user;

import com.example.demo.common.exceptions.BaseException;
import com.example.demo.common.response.BaseResponse;
import com.example.demo.common.response.BaseResponseStatus;
import com.example.demo.common.secret.Secret;
import com.example.demo.src.Open.dto.GetOpenAIReqDto;
import com.example.demo.src.Open.dto.Message;
import com.example.demo.src.tree.TreeRepository;
import com.example.demo.src.tree.entity.Tree;
import com.example.demo.src.user.DTO.*;
import com.example.demo.src.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final TreeRepository treeRepository;
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
        if(tree.getScore() < 50){
            treeList.remove(index-1);
        }
        List<GetOneTree> getOneTreeList = treeList.stream().map(e -> new GetOneTree(e.getSize(),e.getColor())).collect(Collectors.toList());

        GetTrees getTrees= new GetTrees(user.getScore(),tree.getScore(),getOneTreeList,new GetOneTree(tree.getSize(), tree.getColor()));

        return getTrees;

    }
//    public String getReceipt(String url1) throws BaseException {
//
////        try {
////            URL url = new URL(url1);
////            String postData = "foo1=bar1&foo2=bar2";
////            Images images = new Images();
////            images.setUrl(url1);
////            requestBody.put("images", images);
////        requestBody.put("requestId", "string");
////        requestBody.put("resultType", "string");
////        requestBody.put("timestamp", "");
////        requestBody.put("version", "V1");
////
////
////            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
////            conn.setRequestMethod("POST");
////            conn.setDoOutput(true);
////            conn.setRequestProperty("Content-Type", "application/json");
////            conn.setRequestProperty("X-OCR-SECRET", Secret.CLOVA_SECRET_KEY);
////            conn.setUseCaches(false);
////
////            try (DataOutputStream dos = new DataOutputStream(conn.getOutputStream())) {
////                dos.writeBytes(postData);
////            }
////
////            try (BufferedReader br = new BufferedReader(new InputStreamReader(
////                    conn.getInputStream())))
////            {
////                String line;
////                while ((line = br.readLine()) != null) {
////                    System.out.println(line);
////                }
////            }
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
//
//
//        String ENDPOINT = "https://eqkyhw38bo.apigw.ntruss.com/custom/v1/23707/5503778efc7906255bde5996d820ed721e318f1873624979d4f420eab2c5ae91/general";
//        Images images = new Images();
//        images.setUrl(url1);
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        //TODO: 시크릿키 등록
//        headers.set("X-OCR-SECRET", Secret.CLOVA_SECRET_KEY);
//
//        Map<String, Object> requestBody = new HashMap<>();
//
//
//        // Message: model, role
//        requestBody.put("images", images);
//        requestBody.put("requestId", "string");
//        requestBody.put("resultType", "string");
//        requestBody.put("timestamp", "");
//        requestBody.put("version", "V1");
//
//        try {
//            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
//
//            RestTemplate restTemplate = new RestTemplate();
//            System.out.println("sjfslkdjfsjdslkjf");
//            ResponseEntity<Map> response = restTemplate.postForEntity(ENDPOINT, requestEntity, Map.class);
//            System.out.println("jjfkdjkfdkfkdkfjdkjf");
//            // answer 추출
//            System.out.println(response.getBody());
//            //String[] responses = response.getBody().get("choices").toString().split("role=assistant, content=|}, finish_reason=stop, index=0}");
//            //GetOpenAIReqDto getOpenAIResDto = new GetOpenAIReqDto(responses[1]);
//            return "test";
//        }
//        catch (Exception e){
//            throw new BaseException(BaseResponseStatus.OPENAI_ERROR);
//        }
//
//    }
}
