package com.example.demo.src.user;

import com.example.demo.common.exceptions.BaseException;
import com.example.demo.common.response.BaseResponse;
import com.example.demo.src.user.DTO.GetTreeDetail;
import com.example.demo.src.user.DTO.GetTrees;
import com.example.demo.utils.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("")
@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.")})
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    /**
     * 자신이 키우는 나무 출력
     */
    @Tag(name = "자신이 키우는 나무 출력 API")

    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "일치하는 유저가 없습니다."
                    , content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "데이터베이스 연결에 실패하였습니다."
                    , content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @Parameters({
            @Parameter(name = "userId", description = "유저인덱스", example = "1"),
    })
    @Operation(summary = "유저가 키우는 나무 출력", description = "유저가 키우는 나무 디테일 출력 API")
    @GetMapping("/user/{userId}")
    public BaseResponse<GetTreeDetail> getOneTree(@PathVariable("userId") Long userId) {

        try {
            return new BaseResponse<>(userService.getOneTree(userId));
        } catch (BaseException exception) {
            log.error(exception.getMessage());
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 자신의 나무 리스트 출력 API
     */
    @Tag(name = "자신의 나무 리스트 출력 API")

    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "일치하는 유저가 없습니다."
                    , content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "데이터베이스 연결에 실패하였습니다."
                    , content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @Parameters({
            @Parameter(name = "userId", description = "유저인덱스", example = "1"),
    })
    @Operation(summary = "유저가 키우는 나무들 출력", description = "유저가 키우는 나무들 API")
    @GetMapping("/trees/{userId}")
    public BaseResponse<GetTrees> getTrees(@PathVariable("userId") Long userId) {

        try {
            return new BaseResponse<>(userService.getTrees(userId));
        } catch (BaseException exception) {
            log.error(exception.getMessage());
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * current tree 생성 API
     */
    @Tag(name = "current tree 생성 API")

    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "일치하는 유저가 없습니다."
                    , content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "데이터베이스 연결에 실패하였습니다."
                    , content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @Parameters({
            @Parameter(name = "userId", description = "유저인덱스", example = "1"),
    })
    @Operation(summary = "current tree 생성", description = "current tree 생성API")
    @PostMapping("/current/{userId}")
    public BaseResponse<String> createTree(@PathVariable("userId") Long userId) {

        try {
            return new BaseResponse<>(userService.createTree(userId));
        } catch (BaseException exception) {
            log.error(exception.getMessage());
            return new BaseResponse<>(exception.getStatus());
        }
    }
//    @Tag(name = "자신이 키우는 나무 출력 API")
//
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "404", description = "일치하는 유저가 없습니다."
//                    , content = @Content(schema = @Schema(implementation = BaseResponse.class))),
//            @ApiResponse(responseCode = "500", description = "데이터베이스 연결에 실패하였습니다."
//                    , content = @Content(schema = @Schema(implementation = BaseResponse.class)))
//    })
//    @Operation(summary = "유저가 키우는 나무 출력", description = "유저가 키우는 나무 디테일 출력 API")
//    @GetMapping("/receipt")
//    public BaseResponse<String> getOneTree(@RequestParam(required = true) String url) {
//
//        try {
//            return new BaseResponse<>(userService.getReceipt(url));
//        } catch (BaseException exception) {
//            log.error(exception.getMessage());
//            return new BaseResponse<>(exception.getStatus());
//        }
//    }
}