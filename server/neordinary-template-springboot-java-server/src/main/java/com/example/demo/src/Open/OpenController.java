package com.example.demo.src.Open;

import com.example.demo.common.exceptions.BaseException;
import com.example.demo.common.response.BaseResponse;
import com.example.demo.common.response.BaseResponseStatus;
import com.example.demo.common.secret.Secret;
import com.example.demo.src.Open.dto.GetOpenAIReqDto;
import com.example.demo.src.Open.dto.Message;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.ValidationRegex;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/opens")
public class OpenController {;
    private final JwtService jwtService;
    private static final String ENDPOINT = "https://api.openai.com/v1/chat/completions";
    /**
     * OpenAI 키워드  - 키워드 입력 시 penAI 조회 API
     * @param keyword 글 작성 시 포함하고 싶은 내용
     * @param  format 글 작성 형태
     * @return OpenAI 결과 String
     */

    @Tag(name = "OPENAI API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.")
    })

    @Operation(summary = "openAI 요청", description = "글 작성을 위한 openAI API")
    @Parameters({
            @Parameter(name = "keyword", description = "글에 포함할 내용", example = "미래의 나에게"),
            @Parameter(name = "format", description = "chatGPT가 생성할 형식", example = "편지"),
    })
    @GetMapping("/result")
    public BaseResponse<GetOpenAIReqDto> getOpenAI(@RequestParam(required = true) String keyword, @RequestParam String format) throws BaseException {
        if(format.isEmpty()){
            format = "편지";
        }
        // 프로프트 생성
        String prompt = keyword + ", " + format + " 형식으로 50자 이내로 작성해줘.";

        // header 등록: Content-Type, Authorization(API KEY), Message
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        //TODO: 시크릿키 등록
        headers.set("Authorization", "Bearer " + "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");

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
            String[] responses = response.getBody().get("choices").toString().split("role=assistant, content=|}, finish_reason=stop, index=0}");
            GetOpenAIReqDto getOpenAIResDto = new GetOpenAIReqDto(responses[1]);
            return new BaseResponse<>(getOpenAIResDto);
        }
        catch (Exception e){
            throw new BaseException(BaseResponseStatus.OPENAI_ERROR);
        }
    }
}
