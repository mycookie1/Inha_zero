package com.example.demo.src.user.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GetTreeDetail {
    @Schema(name = "score", example = "999", description = "자신의 전체 점수")
    private Integer totalScore;
    @Schema(name = "score", example = "20", description = "자신이 키우는 나무 점수")
    private Integer score;
    @Schema(name = "color", example = "color", description = "1: 2: 3:")
    private Integer color;
    @Schema(name = "size", example = "size", description = "1:씨앗 2:중간 3:거목")
    private Integer size;


}
