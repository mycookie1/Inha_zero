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
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Random;
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
        int sum = 0;
        for(Tree tree: treeList){
            sum += tree.getScore();
        }
        user.setScore(sum);
        userRepository.save(user);

        Random rand = new Random();
        int size = rand.nextInt(3);
        int color = rand.nextInt(3);
        Tree tree = new Tree(size+1,color+1,0);
        tree.setUserId(user);
        Tree savedTree = treeRepository.save(tree);

        return savedTree.getTreeId()+"번 나무생성";
    }

}
