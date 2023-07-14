package com.example.demo.src.user.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class GetScore {
    @Schema(name = "totalScore", example = "10", description = "전체점수")
    private Integer totalScore;
    @Schema(name = "totalScoreIs", example = "true", description = "true: 150달성, false: 달성 실패")
    private Boolean totalScoreIs;
    @Schema(name = "currentTreeScore", example = "10", description = "현재 나무 점수")
    private Integer currentTreeScore;
    @Schema(name = "currentTreeScoreIs", example = "false", description = "true: 50달성, false: 달성 실패")
    private Boolean currentTreeScoreIs;
}
